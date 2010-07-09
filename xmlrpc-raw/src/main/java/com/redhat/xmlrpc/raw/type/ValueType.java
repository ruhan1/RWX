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

package com.redhat.xmlrpc.raw.type;

import org.apache.commons.codec.binary.Base64;

import com.redhat.xmlrpc.raw.error.CoercionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public enum ValueType
{
    NIL( null, "nil" ),
    ARRAY( null, "array" ),
    STRUCT( null, "struct" ),

    INT( new ValueCoercion()
    {
        @Override
        public Object coerce( final String value )
        {
            return Integer.parseInt( value );
        }
    }, "i4", "int" ),

    BOOLEAN( new ValueCoercion()
    {
        @Override
        public Object coerce( final String value )
        {
            return Boolean.valueOf( value );
        }
    }, "boolean" ),

    STRING( new ValueCoercion()
    {
        @Override
        public Object coerce( final String value )
        {
            return value;
        }
    }, "string" ),

    DOUBLE( new ValueCoercion()
    {
        @Override
        public Object coerce( final String value )
        {
            return Double.parseDouble( value );
        }
    }, "double" ),

    DATETIME( new ValueCoercion()
    {
        @Override
        public Object coerce( final String value )
            throws CoercionException
        {
            try
            {
                return new SimpleDateFormat( "yyyyMMdd'T'HHmmss" ).parse( value );
            }
            catch ( final ParseException e )
            {
                throw new CoercionException( "Cannot parse date: '" + value + "'.", e );
            }
        }
    }, "dateTime.iso8601" ),

    BASE64( new ValueCoercion()
    {
        @Override
        public Object coerce( final String value )
        {
            return Base64.decodeBase64( value );
        }
    }, "base64" ), ;

    private String[] tags;

    private ValueCoercion coercion;

    private ValueType( final ValueCoercion coercion, final String... tags )
    {
        this.coercion = coercion;
        this.tags = tags;
    }

    public ValueCoercion coercion()
    {
        return coercion;
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
