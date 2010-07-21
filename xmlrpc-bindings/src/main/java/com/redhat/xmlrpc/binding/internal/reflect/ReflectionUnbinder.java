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

package com.redhat.xmlrpc.binding.internal.reflect;

import static com.redhat.xmlrpc.binding.anno.AnnotationUtils.getRequestMethod;
import static com.redhat.xmlrpc.binding.anno.AnnotationUtils.isRequest;
import static com.redhat.xmlrpc.binding.anno.AnnotationUtils.isResponse;

import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.ArrayRecipe;
import com.redhat.xmlrpc.binding.recipe.FieldBinding;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.binding.recipe.StructRecipe;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.error.XmlRpcFaultException;
import com.redhat.xmlrpc.spi.XmlRpcGenerator;
import com.redhat.xmlrpc.spi.XmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReflectionUnbinder
    implements XmlRpcGenerator
{

    private final Map<Class<?>, ValueType> typeCache = new HashMap<Class<?>, ValueType>();

    private final Map<Class<?>, Recipe<?>> recipesByClass;

    private final Object message;

    public ReflectionUnbinder( final Object message, final Map<Class<?>, Recipe<?>> recipes )
        throws BindException
    {
        this.message = message;
        recipesByClass = recipes;
    }

    @Override
    public ReflectionUnbinder generate( final XmlRpcListener listener )
        throws XmlRpcException
    {
        if ( message instanceof XmlRpcFaultException )
        {
            final XmlRpcFaultException error = (XmlRpcFaultException) message;

            listener.startResponse();
            listener.fault( error.getCode(), error.getReason() );
            listener.endResponse();

            return this;
        }

        final String method = getRequestMethod( message );
        if ( method != null )
        {
            listener.startRequest();
            listener.requestMethod( method );
        }
        else if ( isResponse( message ) )
        {
            listener.startResponse();
        }
        else
        {
            throw new BindException( "Can only unbind classes annotated with either @Request or @Response. Class: "
                + message.getClass().getName() );
        }

        fireMessageEvents( listener );

        if ( isRequest( message ) )
        {
            listener.endRequest();
        }
        else if ( isResponse( message ) )
        {
            listener.endResponse();
        }

        return this;
    }

    @SuppressWarnings( "unchecked" )
    private void fireMessageEvents( final XmlRpcListener listener )
        throws XmlRpcException
    {
        final Class<?> messageCls = message.getClass();
        final Recipe<Integer> recipe = (Recipe<Integer>) recipesByClass.get( messageCls );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for message: " + messageCls.getName() );
        }

        final Map<Integer, FieldBinding> bindings = new TreeMap<Integer, FieldBinding>( recipe.getFieldBindings() );

        for ( final Map.Entry<Integer, FieldBinding> entry : bindings.entrySet() )
        {
            listener.startParameter( entry.getKey() );
            final Object value = fireValueEvents( entry.getValue(), message, listener );

            listener.parameter( entry.getKey(), value, typeOf( value, entry.getValue() ) );
            listener.endParameter();
        }
    }

    @SuppressWarnings( "unchecked" )
    private List<Object> fireArrayEvents( final Class<?> type, final Object part, final XmlRpcListener listener )
        throws XmlRpcException
    {
        final Recipe<Integer> recipe = (Recipe<Integer>) recipesByClass.get( type );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for array value: " + type );
        }

        final Map<Integer, FieldBinding> bindings = new TreeMap<Integer, FieldBinding>( recipe.getFieldBindings() );

        listener.startArray();

        final List<Object> result = new ArrayList<Object>();
        for ( final Map.Entry<Integer, FieldBinding> entry : bindings.entrySet() )
        {
            listener.startArrayElement( entry.getKey() );

            final Object value = fireValueEvents( entry.getValue(), part, listener );

            while ( result.size() < entry.getKey() )
            {
                result.add( null );
            }

            result.add( value );

            listener.arrayElement( entry.getKey(), value, typeOf( value, entry.getValue() ) );
            listener.endArrayElement();
        }

        listener.endArray();

        return result;
    }

    @SuppressWarnings( "unchecked" )
    private Map<String, Object> fireStructEvents( final Class<?> type, final Object part, final XmlRpcListener listener )
        throws XmlRpcException
    {
        final Recipe<String> recipe = (Recipe<String>) recipesByClass.get( type );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for array value: " + type );
        }

        listener.startStruct();

        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        for ( final Map.Entry<String, FieldBinding> entry : recipe.getFieldBindings().entrySet() )
        {
            listener.startStructMember( entry.getKey() );

            final Object value = fireValueEvents( entry.getValue(), part, listener );

            result.put( entry.getKey(), value );

            listener.structMember( entry.getKey(), value, typeOf( value, entry.getValue() ) );
            listener.endStructMember();
        }

        listener.endStruct();

        return result;
    }

    private ValueType typeOf( final Object value, final FieldBinding binding )
        throws BindException
    {
        if ( value == null )
        {
            return ValueType.NIL;
        }

        final Class<?> cls = value.getClass();
        ValueType type = typeCache.get( cls );
        if ( type == null )
        {
            if ( binding != null && recipesByClass.containsKey( binding.getFieldType() ) )
            {
                final Recipe<?> recipe = recipesByClass.get( binding.getFieldType() );

                if ( recipe instanceof ArrayRecipe )
                {
                    type = ValueType.ARRAY;
                }
                else if ( recipe instanceof StructRecipe )
                {
                    type = ValueType.STRUCT;
                }
                else
                {
                    throw new BindException( "Unknown recipe reference type: " + binding.getFieldType() + "\nField: "
                        + binding.getFieldName() );
                }
            }
            else
            {
                type = ValueType.typeFor( value );
            }

            if ( type != null )
            {
                typeCache.put( cls, type );
            }
        }

        return type;
    }

    private Object fireValueEvents( final FieldBinding binding, final Object parent, final XmlRpcListener listener )
        throws XmlRpcException
    {
        final Class<?> parentCls = parent.getClass();
        try
        {
            // TODO: What if the declared field is in the parent's parent?? Does this code handle it?
            Field field = null;
            Class<?> fieldOwner = parentCls;
            while ( field == null && fieldOwner != null )
            {
                try
                {
                    field = fieldOwner.getDeclaredField( binding.getFieldName() );
                }
                catch ( final NoSuchFieldException e )
                {
                    // TODO: log this to debug log-level.
                    field = null;
                    fieldOwner = fieldOwner.getSuperclass();
                }
            }

            if ( field == null )
            {
                throw new BindException( "Cannot find field: " + binding.getFieldName()
                    + " in class (or parent classes of): " + parentCls.getName() );
            }

            field.setAccessible( true );

            Object value = field.get( parent );
            final ValueType type = typeOf( value, binding );

            // FIXME: Invert this logic, and look for @Raw before deciding whether to fire array/struct events...
            if ( recipesByClass.containsKey( binding.getFieldType() ) )
            {
                if ( type == ValueType.ARRAY )
                {
                    value = fireArrayEvents( binding.getFieldType(), value, listener );
                }
                else if ( type == ValueType.STRUCT )
                {
                    value = fireStructEvents( binding.getFieldType(), value, listener );
                }
                else
                {
                    throw new BindException( "Unknown recipe reference type: " + binding.getFieldType() + "\nField: "
                        + binding.getFieldName() + "\nClass: " + parentCls.getName() );
                }
            }
            else
            {
                value = type.coercion().toString( value );
            }

            listener.value( value, type );

            return value;
        }
        catch ( final IllegalAccessException e )
        {
            throw new BindException( "Cannot retrieve field: " + binding.getFieldName() + " in class: "
                + parentCls.getName() + "\nError: " + e.getMessage(), e );
        }
    }

}
