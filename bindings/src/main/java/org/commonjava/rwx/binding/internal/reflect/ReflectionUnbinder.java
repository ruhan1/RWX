/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
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

import org.apache.commons.lang.StringUtils;
import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.Converter;
import org.commonjava.rwx.binding.anno.SkipContainedNull;
import org.commonjava.rwx.binding.anno.SkipNull;
import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx.binding.conf.BindingConfiguration;
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
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;
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

import static org.commonjava.rwx.binding.anno.AnnotationUtils.getRequestMethod;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.hasAnnotation;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.isMessage;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.isRequest;
import static org.commonjava.rwx.binding.anno.AnnotationUtils.isResponse;
import static org.commonjava.rwx.vocab.ValueType.NIL;

public class ReflectionUnbinder
        implements XmlRpcGenerator
{

    private final Map<Class<?>, ValueType> typeCache = new HashMap<Class<?>, ValueType>();

    private final Map<Class<?>, Mapping<?>> recipesByClass;

    private BindingConfiguration configuration;

    private final Object message;

    public ReflectionUnbinder( final Object message, final Map<Class<?>, Mapping<?>> recipes, BindingConfiguration configuration )
            throws BindException
    {
        this.message = message;
        recipesByClass = recipes;
        this.configuration = configuration;
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
            FieldBinding binding = new FieldBinding( "NONE; TOP-LEVEL RENDER", message.getClass(), Void.class );
            fireStructEvents( binding, message, listener );
        }
        else if ( hasAnnotation( message, ArrayPart.class ) )
        {
            FieldBinding binding = new FieldBinding( "NONE; TOP-LEVEL RENDER", message.getClass(), Void.class );
            fireArrayEvents( binding, message, listener );
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

        for ( Map.Entry<Integer, FieldBinding> entry : bindings.entrySet() )
        {
            int index = entry.getKey();
            FieldBinding fieldBinding = entry.getValue();

            fireValueEvents( fieldBinding, message, listener, true, ( val, t ) -> {
                listener.startParameter( index );
                return true;
            }, ( val, t ) -> {
                listener.parameter( index, val, t );
                listener.endParameter();
                return true;
            } );
        }
    }

    @SuppressWarnings( "unchecked" )
    private List<Object> fireArrayEvents( final FieldBinding binding, final Object part, final XmlRpcListener listener )
            throws XmlRpcException
    {
        Class<?> fieldType = binding.getFieldType();
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "Getting mapping recipe for: {}", fieldType.getSimpleName() );
        final Mapping<Integer> recipe = (Mapping<Integer>) recipesByClass.get( binding.getFieldType() );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for array field: " + binding.getFieldName() + " in: " + binding
                    .getOwningType()
                    .getName() + " with field type: " + fieldType.getName() );
        }

        final Map<Integer, FieldBinding> bindings = new TreeMap<Integer, FieldBinding>( recipe.getFieldBindings() );

        listener.startArray();

        final List<Object> result = new ArrayList<Object>();

        for ( Map.Entry<Integer, FieldBinding> entry : bindings.entrySet() )
        {
            int index = entry.getKey();
            FieldBinding fieldBinding = entry.getValue();

            Object arrayEntry = fireValueEvents( fieldBinding, part, listener, false, ( val, t ) -> {
                listener.startArrayElement( index );
                return true;
            }, ( val, t ) -> {
                while ( result.size() < index )
                {
                    result.add( null );
                }

                result.add( val );

                listener.arrayElement( index, val, t );
                listener.endArrayElement();
                return true;
            } );
        }

        listener.endArray();

        return result;
    }

    @SuppressWarnings( "unchecked" )
    private Map<String, Object> fireStructEvents( final FieldBinding binding, final Object part,
                                                  final XmlRpcListener listener )
            throws XmlRpcException
    {
        Class<?> fieldType = binding.getFieldType();
        final Mapping<String> recipe = (Mapping<String>) recipesByClass.get( binding.getFieldType() );

        if ( recipe == null )
        {
            throw new BindException( "Cannot find recipe for struct field: " + binding.getFieldName() + " in: " + binding
                    .getOwningType()
                    .getName() + " with field type: " + fieldType.getName() );
        }

        listener.startStruct();

        Map<String, FieldBinding> bindings = recipe.getFieldBindings();

        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        for ( Map.Entry<String, FieldBinding> entry : bindings.entrySet() )
        {
            String memberName = entry.getKey();
            FieldBinding fieldBinding = entry.getValue();
            Object arrayEntry = fireValueEvents( fieldBinding, part, listener, false, ( val, t ) -> {
                listener.startStructMember( memberName );
                return true;
            }, ( val, t ) -> {
                result.put( memberName, val );

                listener.structMember( memberName, val, t );
                listener.endStructMember();
                return true;
            } );
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
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.debug( "Looking for ValueType of: {} (of class: {}) for field binding: {}", value,
                      ( value == null ? "NONE" : value.getClass().getName() ), binding );

        if ( value == null )
        {
            logger.debug( "Value is null, returning NIL" );
            return NIL;
        }

        if ( value == null && binding == null )
        {
            throw new BindException( "Cannot find ValueType. Both value and FieldBinding are null!" );
        }

        final Class<?> cls = value.getClass();

        logger.debug( "Looking for ValueType of: {}", cls );

        ValueType type = typeCache.get( cls );
        if ( type == null )
        {
            logger.debug( "Lookup recipe for: {}", cls );
            if ( binding != null && recipesByClass.containsKey( cls ) )
            {
                final Mapping<?> recipe = recipesByClass.get( cls );

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
                logger.debug( "Falling back to ValueType.typeFor({})", value );
                type = ValueType.typeFor( value );
            }

            if ( type != null )
            {
                typeCache.put( cls, type );
            }
        }

        return type == null ? NIL : type;
    }

    public interface EventCallback
    {
        boolean call( Object val, ValueType type ) throws XmlRpcException;
    }

    private Object fireValueEvents( final FieldBinding binding, final Object parent, final XmlRpcListener listener,
                                    boolean ignoreSkipNulls,
                                    EventCallback before, EventCallback after )
            throws XmlRpcException
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        final Class<?> parentCls = parent.getClass();
        try
        {
            final Field field = findField( binding, parentCls );

            field.setAccessible( true );

            Object value = field.get( parent );

            if ( !ignoreSkipNulls && isNullSuppressed( field, parentCls, value ) )
            {
                logger.debug( "Skipping null value for: {}", field );
                return null;
            }
//            else if ( value == null )
//            {
//                listener.value( null, ValueType.NIL );
//                return null;
//            }

            ValueType type = typeOf( value, binding );
            logger.debug( "ValueType for {} in binding: {} is: {}.", value, binding, type );

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
                logger.debug( "Calling: {}.generate(..)", vc.getClass().getName() );
                vc.generate( listener, value, recipesByClass );
            }
            else if ( recipesByClass.containsKey( binding.getFieldType() ) )
            {
                logger.debug( "Finding appropriate event-firing mechanism for field: {} with value-type: {} (value is: {})", binding, type, value );
                if ( type == ValueType.ARRAY )
                {
                    logger.debug( "Firing array events for: {}", binding );
                    value = fireArrayEvents( binding, value, listener );
                }
                else if ( type == ValueType.STRUCT )
                {
                    logger.debug( "Firing struct events for: {}", binding );
                    value = fireStructEvents( binding, value, listener );
                }
                else if ( type != NIL )
                {
                    throw new BindException( "Unknown recipe reference type: " + binding.getFieldType() + "\nValue: " + value + "\nField: "
                                                     + binding.getFieldName() + "\nClass: " + parentCls.getName() + "\nAll recipe classes:\n\n  " + StringUtils
                            .join( recipesByClass.keySet(), "\n  ") );
                }

                logger.debug( "Firing value event for: {} on binding: {} with ValueType: {}", value, binding, type );
                listener.value( value, type );
            }
            else
            {
                logger.debug(
                        "No recipe found for field: {} with value-type: {} (value is: {}). Trying to fire raw map/collection events...",
                        binding, type, value );

                final Contains contains = field.getAnnotation( Contains.class );
                final SkipContainedNull skipContainedNull = field.getAnnotation( SkipContainedNull.class );
                if ( Map.class.isAssignableFrom( binding.getFieldType() ) )
                {
                    logger.debug( "Firing map events for: {}", binding );
                    type = ValueType.STRUCT;
                    fireMapEvents( value, binding, contains, skipContainedNull, listener );
                }
                else if ( binding.getFieldType().isArray() || Collection.class.isAssignableFrom(
                        binding.getFieldType() ) )
                {
                    logger.debug( "Firing collection events for: {}", binding );
                    type = ValueType.ARRAY;
                    fireCollectionEvents( value, binding, contains, skipContainedNull, listener );
                }
                else
                {
                    ValueCoercion coercion = type.coercion();
                    if ( coercion == null )
                    {
                        throw new XmlRpcException(
                                "Cannot render {} (type: {}) to string. It has no corresponding coercion, and isn't an @ArrayPart or a @StructPart!" );
                    }
                }

                logger.debug( "Firing value event for: {} on binding: {} with ValueType: {}", value, binding, type );
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

    private boolean isNullSuppressed( Field field, Class<?> cls, Object value )
    {
        if ( value == null )
        {
            SkipNull fieldSkipNull = field.getAnnotation( SkipNull.class );
            if ( fieldSkipNull != null )
            {
                return fieldSkipNull.value();
            }

            SkipNull clsSkipNull = cls.getAnnotation( SkipNull.class );
            if ( clsSkipNull != null )
            {
                return clsSkipNull.value();
            }

            return configuration.isSkipNulls();
        }

        return false;
    }

    private boolean isNullSuppressed( SkipContainedNull fieldSkipContained, Class<?> cls, Object value )
    {
        if ( value == null )
        {
            if ( fieldSkipContained != null )
            {
                return fieldSkipContained.value();
            }

            SkipContainedNull clsSkipContained = cls.getAnnotation( SkipContainedNull.class );
            if ( clsSkipContained != null )
            {
                return clsSkipContained.value();
            }

            return configuration.isSkipNulls();
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
    private void fireMapEvents( final Object values, FieldBinding binding, final Contains contains,
                                final SkipContainedNull skipContainedNull, final XmlRpcListener listener )
            throws XmlRpcException
    {
        if ( values == null )
        {
            return;
        }

        ValueType vt = typeOf( contains );

        boolean structStarted = false;

        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.debug( "Firing events for map entries in: {}", binding );
        for ( final Map.Entry<Object, Object> entry : ( (Map<Object, Object>) values ).entrySet() )
        {
            final String key = String.valueOf( entry.getKey() );
            Object value = entry.getValue();
            if ( isNullSuppressed( skipContainedNull, binding.getOwningType(), value ) )
            {
                continue;
            }

            final Class<?> type = value.getClass();

            binding = new FieldBinding( binding.getFieldName() + "<Map-Value>", type, binding.getOwningType() );
            if ( vt == null )
            {
                vt = typeOf( value, binding );
            }

            if ( !structStarted )
            {
                listener.startStruct();
                structStarted = true;
            }

            listener.startStructMember( key );

            if ( ValueType.ARRAY == vt )
            {
                fireArrayEvents( binding, value, listener );
            }
            else if ( ValueType.STRUCT == vt )
            {
                fireStructEvents( binding, value, listener );
            }
            else
            {
                value = vt.coercion().toString( value );
            }

            listener.value( value, vt );

            listener.structMember( key, entry.getValue(), vt );
            listener.endStructMember();
        }

        if ( structStarted )
        {
            listener.endStruct();
        }

    }

    @SuppressWarnings( "unchecked" )
    private void fireCollectionEvents( final Object values, FieldBinding binding, final Contains contains,
                                       SkipContainedNull skipContainedNull, final XmlRpcListener listener )
            throws XmlRpcException
    {
        if ( values == null )
        {
            return;
        }

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
            if ( isNullSuppressed( skipContainedNull, binding.getOwningType(), entry ) )
            {
                continue;
            }

            final Class<?> type = entry.getClass();

            binding = new FieldBinding( binding.getFieldName() + "<Collection-Value>", type, binding.getOwningType() );
            if ( vt == null )
            {
                vt = typeOf( entry, binding );
            }

            if ( vt == null )
            {
                throw new CoercionException( "Cannot find suitable coercion for field: " + binding.getFieldName() + " in: " + type.getName() + ", with collection type: " + ( contains == null ?
                        "Un-annotated collection" :
                        contains.value() ) );
            }

            listener.startArrayElement( i );

            if ( ValueType.ARRAY == vt )
            {
                fireArrayEvents( binding, entry, listener );
            }
            else if ( ValueType.STRUCT == vt )
            {
                fireStructEvents( binding, entry, listener );
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
