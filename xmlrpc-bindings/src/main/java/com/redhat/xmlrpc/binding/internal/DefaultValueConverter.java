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

package com.redhat.xmlrpc.binding.internal;

import com.redhat.xmlrpc.binding.BinderyContext;
import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.spi.ValueConverter;
import com.redhat.xmlrpc.model.ValueType;
import com.redhat.xmlrpc.raw.model.XmlRpcArray;
import com.redhat.xmlrpc.raw.model.XmlRpcSingleValue;
import com.redhat.xmlrpc.raw.model.XmlRpcStruct;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import java.util.List;
import java.util.Map;

public class DefaultValueConverter
    implements ValueConverter
{

    @Override
    public Object parse( final XmlRpcValue<?> src, final BinderyContext context )
    {
        return src.getValue();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public XmlRpcValue<?> render( final Object src, final BinderyContext context )
        throws BindException
    {
        XmlRpcValue<?> result = null;
        if ( src instanceof Map )
        {
            final Map<Object, Object> values = (Map<Object, Object>) src;

            final XmlRpcStruct struct = new XmlRpcStruct();
            result = struct;
            for ( final Map.Entry<Object, Object> entry : values.entrySet() )
            {
                final String key = String.valueOf( entry.getKey() );
                final XmlRpcValue<?> val = render( entry.getValue(), context );
                struct.put( key, val );
            }
        }
        else if ( src instanceof List )
        {
            final List<Object> values = (List<Object>) src;
            final XmlRpcArray array = new XmlRpcArray();
            result = array;

            for ( final Object val : values )
            {
                array.add( render( val, context ) );
            }
        }
        else
        {
            final ValueType vt = ValueType.typeFor( src );
            if ( vt == null )
            {
                throw new BindException( "ValueType not found for: " + src.getClass().getName() );
            }

            result = new XmlRpcSingleValue( src, vt );
        }

        return result;
    }

}
