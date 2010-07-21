/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.xmlrpc.binding.internal.xbr;

import static com.redhat.xmlrpc.binding.recipe.RecipeUtils.toStringArray;

import org.apache.xbean.recipe.ConstructionException;
import org.apache.xbean.recipe.DefaultRepository;
import org.apache.xbean.recipe.ObjectRecipe;
import org.apache.xbean.recipe.Repository;

import com.redhat.xmlrpc.binding.anno.Request;
import com.redhat.xmlrpc.binding.anno.Response;
import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.ArrayRecipe;
import com.redhat.xmlrpc.binding.recipe.FieldBinding;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.binding.recipe.StructRecipe;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.error.XmlRpcFaultException;
import com.redhat.xmlrpc.spi.AbstractXmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;

import java.util.Map;
import java.util.Stack;

public class XBRBinder<T>
    extends AbstractXmlRpcListener
{

    private final Repository repository = new DefaultRepository();

    private final Map<Class<?>, Recipe<?>> recipes;

    private final Stack<ObjectRecipe> builderStack = new Stack<ObjectRecipe>();

    private final Stack<Recipe<?>> recipeStack = new Stack<Recipe<?>>();

    private Recipe<?> current;

    private ObjectRecipe currentBuilder;

    private FieldBinding currentFieldBinding;

    private final Class<T> messageType;

    public XBRBinder( final Class<T> entryPoint, final Map<Class<?>, Recipe<?>> recipes )
        throws BindException
    {
        this.messageType = entryPoint;

        current = recipes.get( entryPoint );
        if ( current == null )
        {
            throw new BindException( "Recipe mapping doesn't contain recipe for entry-point class: "
                + entryPoint.getName() );
        }

        if ( entryPoint.getAnnotation( Request.class ) == null && entryPoint.getAnnotation( Response.class ) == null )
        {
            throw new BindException( "Invalid entry-point for binding. Class: " + entryPoint.getName()
                + " must be annotated with either @Request or @Response." );
        }

        this.recipes = recipes;

        for ( final Recipe<?> recipe : recipes.values() )
        {
            final ObjectRecipe builder =
                new ObjectRecipe( recipe.getObjectType(), toStringArray( recipe.getConstructorKeys() ) );

            if ( current == recipe )
            {
                currentBuilder = builder;
            }

            repository.add( recipe.getObjectType().getName(), builder );
        }

        for ( final Recipe<?> recipe : recipes.values() )
        {
            final ObjectRecipe r = (ObjectRecipe) repository.get( recipe.getObjectType().getName() );

            final Map<? extends Object, FieldBinding> bindings = recipe.getFieldBindings();

            final String[] ctorKeys = toStringArray( recipe.getConstructorKeys() );
            final String[] ctorArgs = new String[ctorKeys.length];
            for ( int i = 0; i < ctorKeys.length; i++ )
            {
                final FieldBinding binding = bindings.get( ctorKeys[i] );
                ctorArgs[i] = binding.getFieldName();
            }

            r.setConstructorArgNames( ctorArgs );

            for ( final Map.Entry<? extends Object, FieldBinding> entry : bindings.entrySet() )
            {
                final FieldBinding binding = entry.getValue();
                if ( isRecipeReference( binding ) )
                {
                    final ObjectRecipe ref = (ObjectRecipe) repository.get( binding.getFieldType().getName() );
                    if ( ref == null )
                    {
                        throw new BindException( "Cannot find recipe: " + binding.getFieldType() + " for field: "
                            + entry.getKey() + " in: " + recipe.getObjectType() );
                    }

                    r.setProperty( binding.getFieldName(), ref );
                }
            }
        }
    }

    private boolean isRecipeReference( final FieldBinding binding )
    {
        return recipes.containsKey( binding.getFieldType() );
    }

    public T create()
        throws BindException
    {
        while ( builderStack.size() > 1 )
        {
            builderStack.pop();
        }

        // handle the case where the message is completely flat, with only simple parameters.
        final ObjectRecipe builder = builderStack.isEmpty() ? currentBuilder : builderStack.pop();
        try
        {
            return messageType.cast( builder.create() );
        }
        catch ( final ConstructionException e )
        {
            throw new BindException( "Failed to build: " + e.getMessage(), e );
        }
    }

    @Override
    public XBRBinder<T> endParameter()
    {
        popField();
        return this;
    }

    @Override
    public XBRBinder<T> fault( final int code, final String message )
        throws XmlRpcException
    {
        throw new XmlRpcFaultException( code, message );
    }

    @Override
    public XBRBinder<T> startArrayElement( final int index )
    {
        findField( index );
        return this;
    }

    @Override
    public XBRBinder<T> endArrayElement()
    {
        popField();
        return this;
    }

    @Override
    public XBRBinder<T> startParameter( final int index )
    {
        findField( index );
        return this;
    }

    @Override
    public XBRBinder<T> startStructMember( final String key )
    {
        pushCurrent( key );
        return this;
    }

    @Override
    public XBRBinder<T> endStructMember()
    {
        popField();
        return this;
    }

    @Override
    public XBRBinder<T> value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentFieldBinding == null )
        {
            throw new BindException( "Cannot find field for value: " + value + " (type: " + type
                + ")\nCurrent recipe: " + current.getObjectType().getName() );
        }

        currentBuilder.setProperty( currentFieldBinding.getFieldName(), value );
        return this;
    }

    private void findField( final Object key )
    {
        final FieldBinding binding;

        if ( key instanceof Integer )
        {
            binding = ( (ArrayRecipe) current ).getFieldBinding( (Integer) key );
        }
        else
        {
            binding = ( (StructRecipe) current ).getFieldBinding( (String) key );
        }

        if ( isRecipeReference( binding ) )
        {
            pushCurrent( key );
        }
        else
        {
            currentFieldBinding = binding;
        }
    }

    private void popField()
    {
        if ( currentFieldBinding != null )
        {
            currentFieldBinding = null;
        }
        else
        {
            popCurrent();
        }
    }

    private void popCurrent()
    {
        current = recipeStack.pop();
        currentBuilder = builderStack.pop();
    }

    private void pushCurrent( final Object key )
    {
        final FieldBinding binding = current.getFieldBindings().get( key );
        final ObjectRecipe r = (ObjectRecipe) repository.get( binding.getFieldType().getName() );
        final Recipe<?> recipe = recipes.get( binding.getFieldType() );

        recipeStack.push( current );
        builderStack.push( currentBuilder );

        current = recipe;
        currentBuilder = r;
    }

}
