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

package com.redhat.xmlrpc.vocab;

import org.apache.commons.codec.binary.Base64;

import com.redhat.xmlrpc.error.CoercionException;
import com.redhat.xmlrpc.util.ValueCoercion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum ValueType
{
    NIL( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            return null;
        }

        @Override
        public String toString( final Object value )
        {
            return null;
        }
    }, Void.class, "nil" ),

    INT( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
        {
            return Integer.parseInt( value );
        }
    }, Integer.class, "i4", "int" ),

    BOOLEAN( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
        {
            return Boolean.valueOf( value );
        }
    }, Boolean.class, "boolean" ),

    STRING( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
        {
            return value;
        }
    }, String.class, "string" ),

    DOUBLE( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
        {
            return Double.parseDouble( value );
        }

        @Override
        public String toString( final Object value )
        {
            return Double.toString( ( (Number) value ).doubleValue() );
        }
    }, Number.class, "double" ),

    DATETIME( new ValueCoercion()
    {
        private static final String FORMAT = "yyyyMMdd'T'HHmmss";

        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            try
            {
                return new SimpleDateFormat( FORMAT ).parse( value );
            }
            catch ( final ParseException e )
            {
                throw new CoercionException( "Cannot parse date: '" + value + "'.", e );
            }
        }

        @Override
        public String toString( final Object value )
        {
            return new SimpleDateFormat( FORMAT ).format( value );
        }

    }, Date.class, "dateTime.iso8601" ),

    BASE64( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
        {
            return Base64.decodeBase64( value );
        }

        @Override
        public String toString( final Object value )
        {
            return new String( Base64.encodeBase64( (byte[]) value ) );
        }

    }, byte[].class, "base64" ), ;

    private String[] tags;

    private Class<?> nativeType;

    private ValueCoercion coercion;

    private ValueType( final ValueCoercion coercion, final Class<?> nativeType, final String... tags )
    {
        this.coercion = coercion;
        this.nativeType = nativeType;
        this.tags = tags;
    }

    public ValueCoercion coercion()
    {
        return coercion;
    }

    public String getPrimaryTag()
    {
        return tags[0];
    }

    public static ValueType safeTypeFor( final Object value )
    {
        final ValueType type = typeFor( value );
        return type == null ? STRING : type;
    }

    public static ValueType typeFor( final Object value )
    {
        ValueType result = null;

        if ( value == null )
        {
            result = NIL;
        }
        else
        {
            for ( final ValueType vt : values() )
            {
                if ( vt.nativeType.isAssignableFrom( value.getClass() ) )
                {
                    result = vt;
                    break;
                }
            }
        }

        return result;
    }

    public static ValueType typeOf( final String tag )
    {
        if ( tag == null || tag.trim().length() < 1 )
        {
            return STRING;
        }

        for ( final ValueType type : values() )
        {
            for ( final String t : type.tags )
            {
                if ( t.equals( tag ) )
                {
                    return type;
                }
            }
        }

        return STRING;
    }
}
