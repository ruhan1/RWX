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
package org.commonjava.rwx.util;

import org.commonjava.rwx.vocab.Nil;
import org.commonjava.rwx.vocab.ValueType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruhan on 7/13/17.
 */
public class ParseUtils
{
    public static Object nullifyNil( Object object )
    {
        if ( isNil( object ) )
        {
            return null;
        }
        return object;
    }

    public static boolean isNil( Object object )
    {
        return object instanceof Nil;
    }

    /**
     * Upgrade cast, e.g., Int to Long.
     *
     * @param clazz target type
     * @param value object to be casted
     * @return object of target type
     * @throws ClassCastException If not castable, throw ClassCastException
     */
    public static Object upgradeCast( Class clazz, Object value )
    {
        ValueType type = ValueType.typeFor( wrap( clazz ) );
        return type.coercion().upgradeCast( value );
    }

    private static <T> Class<T> wrap( Class<T> c )
    {
        return c.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get( c ) : c;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<>();

    static
    {
        PRIMITIVES_TO_WRAPPERS.put( boolean.class, Boolean.class );
        PRIMITIVES_TO_WRAPPERS.put( byte.class, Byte.class );
        PRIMITIVES_TO_WRAPPERS.put( char.class, Character.class );
        PRIMITIVES_TO_WRAPPERS.put( double.class, Double.class );
        PRIMITIVES_TO_WRAPPERS.put( float.class, Float.class );
        PRIMITIVES_TO_WRAPPERS.put( int.class, Integer.class );
        PRIMITIVES_TO_WRAPPERS.put( long.class, Long.class );
        PRIMITIVES_TO_WRAPPERS.put( short.class, Short.class );
        PRIMITIVES_TO_WRAPPERS.put( void.class, Void.class );
    }
}
