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

import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.FieldBinding;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.binding.recipe.StructRecipe;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.error.XmlRpcFaultException;
import com.redhat.xmlrpc.spi.AbstractXmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class XBRBinder
    extends AbstractXmlRpcListener
{

    private final Repository repository = new DefaultRepository();

    private final Map<String, Recipe<?>> recipes = new HashMap<String, Recipe<?>>();

    private final Stack<ObjectRecipe> builderStack = new Stack<ObjectRecipe>();

    private final Stack<Recipe<?>> recipeStack = new Stack<Recipe<?>>();

    private Recipe<?> current;

    private ObjectRecipe currentBuilder;

    private FieldBinding currentFieldBinding;

    public XBRBinder( final Recipe<?> entryPoint, final Collection<Recipe<?>> recipes )
        throws BindException
    {
        current = entryPoint;
        for ( final Recipe<?> recipe : recipes )
        {
            this.recipes.put( recipe.getName(), recipe );
            repository.add( recipe.getName(), new ObjectRecipe( recipe.getObjectType(),
                                                                toStringArray( recipe.getConstructorKeys() ) ) );
        }

        for ( final Recipe<?> recipe : recipes )
        {
            final ObjectRecipe r = (ObjectRecipe) repository.get( recipe.getName() );

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
                if ( binding.isRecipeReference() )
                {
                    final ObjectRecipe ref = (ObjectRecipe) repository.get( binding.getFieldType() );
                    if ( ref == null )
                    {
                        throw new BindException( "Cannot find recipe: " + binding.getFieldType() + " for field: "
                            + entry.getKey() + " in: " + recipe.getObjectType() );
                    }

                    r.setProperty( binding.getFieldName(), ref );
                }
            }

        }

        currentBuilder = (ObjectRecipe) repository.get( entryPoint.getName() );
    }

    public Object create()
        throws BindException
    {
        while ( builderStack.size() > 1 )
        {
            builderStack.pop();
        }

        final ObjectRecipe builder = builderStack.pop();
        try
        {
            return builder.create();
        }
        catch ( final ConstructionException e )
        {
            throw new BindException( "Failed to build: " + e.getMessage(), e );
        }
    }

    @Override
    public XBRBinder endParameter()
    {
        popField();
        return this;
    }

    @Override
    public XBRBinder fault( final int code, final String message )
        throws XmlRpcException
    {
        throw new XmlRpcFaultException( code, message );
    }

    @Override
    public XBRBinder startArrayElement( final int index )
    {
        final String key = Integer.toString( index );
        findField( key );
        return this;
    }

    @Override
    public XBRBinder endArrayElement()
    {
        popField();
        return this;
    }

    @Override
    public XBRBinder startParameter( final int index )
    {
        final String key = Integer.toString( index );
        findField( key );
        return this;
    }

    @Override
    public XBRBinder startStructMember( final String key )
    {
        pushCurrent( key );
        return this;
    }

    @Override
    public XBRBinder endStructMember()
    {
        popField();
        return this;
    }

    @Override
    public XBRBinder value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentFieldBinding == null )
        {
            throw new BindException( "Cannot find field for value: " + value + " (type: " + type
                + ")\nCurrent recipe: " + current.getName() );
        }

        currentBuilder.setProperty( currentFieldBinding.getFieldName(), value );
        return this;
    }

    private void findField( final Object k )
    {
        final String key = String.valueOf( k );
        final FieldBinding binding = ( (StructRecipe) current ).getFieldBinding( key );
        if ( binding.isRecipeReference() )
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

    private void pushCurrent( final String key )
    {
        final FieldBinding binding = current.getFieldBindings().get( key );
        final ObjectRecipe r = (ObjectRecipe) repository.get( binding.getFieldType() );
        final Recipe<?> recipe = recipes.get( binding.getFieldType() );

        recipeStack.push( current );
        builderStack.push( currentBuilder );

        current = recipe;
        currentBuilder = r;
    }

}
