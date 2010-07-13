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

package com.redhat.xmlrpc.raw.model;

import com.redhat.xmlrpc.model.ValueType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class XmlRpcStruct
    extends LinkedHashMap<String, XmlRpcValue<?>>
    implements XmlRpcValue<Map<String, Object>>
{

    private static final long serialVersionUID = 1L;

    private boolean locked = false;

    public void lock()
    {
        locked = true;
    }

    public XmlRpcStruct withSingleValue( final String key, final Object value, final ValueType type )
    {
        put( key, new XmlRpcSingleValue( value, type ) );
        return this;
    }

    public XmlRpcStruct withStruct( final String key, final XmlRpcStruct struct )
    {
        put( key, struct );
        return this;
    }

    public XmlRpcStruct withArray( final String key, final XmlRpcArray array )
    {
        put( key, array );
        return this;
    }

    public Map<String, Object> rawStruct()
    {
        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        for ( final Map.Entry<String, XmlRpcValue<?>> val : entrySet() )
        {
            result.put( val.getKey(), val.getValue().getValue() );
        }

        return Collections.unmodifiableMap( result );
    }

    @Override
    public void clear()
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "Struct is locked." );
        }

        super.clear();
    }

    @Override
    public XmlRpcValue<?> put( final String key, final XmlRpcValue<?> value )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "Struct is locked." );
        }

        return super.put( key, value );
    }

    @Override
    public void putAll( final Map<? extends String, ? extends XmlRpcValue<?>> m )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "Struct is locked." );
        }

        super.putAll( m );
    }

    @Override
    public XmlRpcValue<?> remove( final Object key )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "Struct is locked." );
        }

        return super.remove( key );
    }

    @Override
    public Map<String, Object> getValue()
    {
        return rawStruct();
    }

}
