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

package com.redhat.xmlrpc.impl.stax.helper;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.spi.XmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;
import com.redhat.xmlrpc.vocab.XmlRpcConstants;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ValueHelper
{

    private static final StructHelper STRUCT_PARSER = new StructHelper();

    private static final ArrayHelper ARRAY_PARSER = new ArrayHelper();

    public ValueType typeOf( final XMLStreamReader reader )
    {
        final String tag = reader.getName().getLocalPart();
        return ValueType.typeOf( tag );
    }

    public Object valueOf( final XMLStreamReader reader, final ValueType type, final XmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        Object value = null;

        final String tag = reader.getName().getLocalPart();
        if ( XmlRpcConstants.STRUCT.equals( tag ) )
        {
            value = STRUCT_PARSER.parse( reader, listener );
        }
        else if ( XmlRpcConstants.ARRAY.equals( tag ) )
        {
            value = ARRAY_PARSER.parse( reader, listener );
        }
        else if ( XmlRpcConstants.VALUE.equals( tag ) )
        {
            final String src = reader.getElementText().trim();

            value = type.coercion().fromString( src );
        }
        else
        {
            throw new XmlRpcException( "Invalid value type: " + tag );
        }

        listener.value( value, type );
        return value;
    }

}
