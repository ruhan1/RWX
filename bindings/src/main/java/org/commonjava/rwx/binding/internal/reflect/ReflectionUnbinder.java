/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.rwx.binding.internal.reflect;

import static org.commonjava.rwx.binding.anno.AnnotationUtils.getRequestMethod;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.hasAnnotation;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.isMessage;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.isRequest;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.isResponse;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.anno.Converter;
import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.SkipNull;
import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.xbr.XBRBinderInstantiator;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.StructMapping;
import org.commonjava.rwx.binding.spi.value.ValueBinder;
import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.impl.stax.helper.ValueHelper;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.util.LambdaHolder;
import org.commonjava.rwx.util.ValueCoercion;
import org.commonjava.rwx.vocab.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReflectionUnbinder
        implements XmlRpcGenerator
{

    private final Map<Class<?>, ValueType> typeCache = new HashMap<Class<?>, ValueType>();

    private final Map<Class<?>, Mapping<?>> recipesByClass;

    private final Object message;

    public ReflectionUnbinder( final Object message, final Map<Class<?>, Mapping<?>> recipes )
            throws BindException
    {
        this.message = message;
        recipesByClass = recipes;
    }

    @SuppressWarnings( "unchecked" )
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

        if ( isMessage( message ) )
        {
            fireMessageEvents( listener );
        }
        else if ( hasAnnotation( message, StructPart.class ) )
        {
            fireStructEvents( message.getClass(), message, listener );
        }
        else if ( hasAnnotation( message, ArrayPart.class ) )
        {
            fireArrayEvents( message.getClass(), message, listener );
        }

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
        final Mapping<Integer> recipe = (Mapping<Integer>) recipesByClass.get( messageCls );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for message: " + messageCls.getName() );
        }

        final Map<Integer, FieldBinding> bindings = new TreeMap<Integer, FieldBinding>( recipe.getFieldBindings() );

        LambdaHolder<XmlRpcException> holder = new LambdaHolder<>();
        bindings.forEach( ( index, fieldBinding ) -> {
            try
            {
                fireValueEvents( fieldBinding, message, listener, ( val, t ) -> {
                    try
                    {
                        listener.startParameter( index );
                        return true;
                    }
                    catch ( XmlRpcException e )
                    {
                        holder.set( e );
                    }
                    return false;
                }, ( val, t ) -> {
                    try
                    {
                        listener.parameter( index, val, t );
                        listener.endParameter();
                        return true;
                    }
                    catch ( XmlRpcException e )
                    {
                        holder.set( e );
                    }
                    return false;
                } );
            }
            catch ( XmlRpcException e )
            {
                holder.set( e );
            }
        } );

        if ( holder.has() )
        {
            throw holder.get();
        }
    }

    @SuppressWarnings( "unchecked" )
    private List<Object> fireArrayEvents( final Class<?> type, final Object part, final XmlRpcListener listener )
            throws XmlRpcException
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "Getting mapping recipe for: {}", type.getSimpleName() );
        final Mapping<Integer> recipe = (Mapping<Integer>) recipesByClass.get( type );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for array value: " + type.getName() );
        }

        final Map<Integer, FieldBinding> bindings = new TreeMap<Integer, FieldBinding>( recipe.getFieldBindings() );

        listener.startArray();

        final List<Object> result = new ArrayList<Object>();

        LambdaHolder<XmlRpcException> holder = new LambdaHolder<>();
        bindings.forEach( ( index, fieldBinding ) -> {
            try
            {
                Object arrayEntry = fireValueEvents( fieldBinding, part, listener, ( val, t ) -> {
                    try
                    {
                        listener.startArrayElement( index );
                        return true;
                    }
                    catch ( XmlRpcException e )
                    {
                        holder.set( e );
                    }
                    return false;
                }, ( val, t ) -> {
                    try
                    {
                        while ( result.size() < index )
                        {
                            result.add( null );
                        }

                        result.add( val );

                        listener.arrayElement( index, val, t );
                        listener.endArrayElement();
                        return true;
                    }
                    catch ( XmlRpcException e )
                    {
                        holder.set( e );
                    }
                    return false;
                } );
            }
            catch ( XmlRpcException e )
            {
                holder.set( e );
            }
        } );

        if ( holder.has() )
        {
            throw holder.get();
        }

        listener.endArray();

        return result;
    }

    @SuppressWarnings( "unchecked" )
    private Map<String, Object> fireStructEvents( final Class<?> type, final Object part,
                                                  final XmlRpcListener listener )
            throws XmlRpcException
    {
        final Mapping<String> recipe = (Mapping<String>) recipesByClass.get( type );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for array value: " + type.getName() );
        }

        listener.startStruct();

        LambdaHolder<XmlRpcException> holder = new LambdaHolder<>();
        Map<String, FieldBinding> bindings = recipe.getFieldBindings();

        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        bindings.forEach( ( memberName, fieldBinding ) -> {
            try
            {
                Object arrayEntry = fireValueEvents( fieldBinding, part, listener, ( val, t ) -> {
                    try
                    {
                        listener.startStructMember( memberName );
                        return true;
                    }
                    catch ( XmlRpcException e )
                    {
                        holder.set( e );
                    }
                    return false;
                }, ( val, t ) -> {
                    try
                    {
                        result.put( memberName, val );

                        listener.structMember( memberName, val, t );
                        listener.endStructMember();
                        return true;
                    }
                    catch ( XmlRpcException e )
                    {
                        holder.set( e );
                    }
                    return false;
                } );
            }
            catch ( XmlRpcException e )
            {
                holder.set( e );
            }
        } );

        if ( holder.has() )
        {
            throw holder.get();
        }

        listener.endStruct();

        return result;
    }

    private ValueType typeOf( final Contains contains )
    {
        ValueType vt = null;
        if ( contains != null )
        {
            final Mapping<?> recipe = recipesByClass.get( contains.value() );
            if ( recipe != null )
            {
                if ( recipe instanceof ArrayMapping )
                {
                    vt = ValueType.ARRAY;
                }
                else if ( recipe instanceof StructMapping )
                {
                    vt = ValueType.STRUCT;
                }
                else
                {
                    vt = ValueType.typeFor( contains.value() );
                }
            }
        }

        return vt;
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
                final Mapping<?> recipe = recipesByClass.get( binding.getFieldType() );

                if ( recipe instanceof ArrayMapping )
                {
                    type = ValueType.ARRAY;
                }
                else if ( recipe instanceof StructMapping )
                {
                    type = ValueType.STRUCT;
                }
                else
                {
                    throw new BindException( "Unknown recipe reference type: " + binding.getFieldType() + "\nField: "
                                                     + binding.getFieldName() + "\nClass: " + cls.getName() );
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

    public interface EventCallback
    {
        boolean call( Object val, ValueType type );
    }

    private Object fireValueEvents( final FieldBinding binding, final Object parent, final XmlRpcListener listener,
                                    EventCallback before, EventCallback after )
            throws XmlRpcException
    {
        final Class<?> parentCls = parent.getClass();
        try
        {
            final Field field = findField( binding, parentCls );

            field.setAccessible( true );

            Object value = field.get( parent );

            if ( isSuppressedNull( field, parentCls, value ) )
            {
                return null;
            }

            final ValueType type = typeOf( value, binding );

            if ( before != null )
            {
                if ( !before.call( value, type ) )
                {
                    return null;
                }
            }

            Converter converter = field.getAnnotation( Converter.class );
            if ( converter == null )
            {
                Class<?> fieldType = field.getType();
                converter = fieldType.getAnnotation( Converter.class );
            }

            if ( converter != null )
            {
                final ValueBinder vc = XBRBinderInstantiator.newValueUnbinder( converter );
                vc.generate( listener, value, recipesByClass );
            }
            else if ( recipesByClass.containsKey( binding.getFieldType() ) )
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

                listener.value( value, type );
            }
            else
            {
                final Contains contains = field.getAnnotation( Contains.class );
                if ( Map.class.isAssignableFrom( binding.getFieldType() ) )
                {
                    fireMapEvents( value, binding.getFieldName(), contains, listener );
                }
                else if ( binding.getFieldType().isArray() || Collection.class.isAssignableFrom(
                        binding.getFieldType() ) )
                {
                    fireCollectionEvents( value, binding.getFieldName(), contains, listener );
                }
                else
                {
                    ValueCoercion coercion = type.coercion();
                    if ( coercion == null )
                    {
                        throw new XmlRpcException(
                                "Cannot render {} (type: {}) to string. It has no corresponding coercion, and isn't an @ArrayPart or a @StructPart!" );
                    }
                    value = coercion.toString( value );
                }

                listener.value( value, type );
            }

            if ( after != null )
            {
                if ( !after.call( value, type ) )
                {
                    return null;
                }
            }

            return value;
        }
        catch ( final IllegalAccessException e )
        {
            throw new BindException(
                    "Cannot retrieve field: " + binding.getFieldName() + " in class: " + parentCls.getName()
                            + "\nError: " + e.getMessage(), e );
        }
    }

    private boolean isSuppressedNull( Field field, Class<?> cls, Object value )
    {
        if ( value == null )
        {
            if ( field.getAnnotation( SkipNull.class ) != null || cls.getAnnotation( SkipNull.class ) != null )
            {
                return true;
            }
        }

        return false;
    }

    private Field findField( final FieldBinding binding, final Class<?> parentCls )
            throws BindException
    {
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
            throw new BindException(
                    "Cannot find field: " + binding.getFieldName() + " in class (or parent classes of): "
                            + parentCls.getName() );
        }

        return field;
    }

    @SuppressWarnings( "unchecked" )
    private void fireMapEvents( final Object values, final String fieldName, final Contains contains,
                                final XmlRpcListener listener )
            throws XmlRpcException
    {
        ValueType vt = typeOf( contains );

        listener.startStruct();

        for ( final Map.Entry<Object, Object> entry : ( (Map<Object, Object>) values ).entrySet() )
        {
            final String key = String.valueOf( entry.getKey() );
            Object value = entry.getValue();
            final Class<?> type = value.getClass();

            if ( vt == null )
            {
                final FieldBinding binding = new FieldBinding( fieldName + "<Map-Value>", type );
                vt = typeOf( value, binding );
            }

            listener.startStructMember( key );

            if ( ValueType.ARRAY == vt )
            {
                fireArrayEvents( type, value, listener );
            }
            else if ( ValueType.STRUCT == vt )
            {
                fireStructEvents( type, value, listener );
            }
            else
            {
                value = vt.coercion().toString( value );
            }

            listener.value( value, vt );

            listener.structMember( key, entry.getValue(), vt );
            listener.endStructMember();
        }

        listener.endStruct();

    }

    @SuppressWarnings( "unchecked" )
    private void fireCollectionEvents( final Object values, final String fieldName, final Contains contains,
                                       final XmlRpcListener listener )
            throws XmlRpcException
    {
        ValueType vt = typeOf( contains );

        listener.startArray();

        Collection<Object> vals;
        if ( values != null && values.getClass().isArray() )
        {
            vals = Arrays.asList( (Object[]) values );
        }
        else
        {
            vals = (Collection<Object>) values;
        }

        int i = 0;
        for ( Object entry : vals )
        {
            final Class<?> type = entry.getClass();

            if ( vt == null )
            {
                final FieldBinding binding = new FieldBinding( fieldName + "<Map-Value>", type );
                vt = typeOf( entry, binding );
            }

            if ( vt == null )
            {
                throw new CoercionException( "Cannot find suitable coercion for type: " + ( contains == null ?
                        "Un-annotated collection" :
                        contains.value() ) );
            }

            listener.startArrayElement( i );

            if ( ValueType.ARRAY == vt )
            {
                fireArrayEvents( type, entry, listener );
            }
            else if ( ValueType.STRUCT == vt )
            {
                fireStructEvents( type, entry, listener );
            }
            else
            {
                entry = vt.coercion().toString( entry );
            }

            listener.value( entry, vt );

            listener.arrayElement( i, entry, vt );
            listener.endArrayElement();

            i++;
        }

        listener.endArray();

    }

}
