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

package com.redhat.xmlrpc.stax.helper;

import com.redhat.xmlrpc.XmlRpcHandler;
import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;
import com.redhat.xmlrpc.raw.type.ValueType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ValueHelper
    implements StaxHelper<XmlRpcValue>
{

    @Override
    public XmlRpcValue parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        final String tag = reader.getName().getLocalPart();
        final String value = reader.getElementText().trim();

        final ValueType type = ValueType.typeOf( tag );
        final Object result = type.coercion().coerce( value );

        return new XmlRpcValue( result, type );
    }

}
