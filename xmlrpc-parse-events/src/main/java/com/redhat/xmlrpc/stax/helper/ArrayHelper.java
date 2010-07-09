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
import com.redhat.xmlrpc.raw.XmlRpcConstants;
import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcArray;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ArrayHelper
    implements StaxHelper<XmlRpcArray>
{

    private static final ValueHelper VALUE_PARSER = new ValueHelper();

    @Override
    public XmlRpcArray parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        handler.startArray();

        final XmlRpcArray array = new XmlRpcArray();

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                if ( XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
                {
                    final XmlRpcValue val = VALUE_PARSER.parse( reader, null );

                    handler.arrayElement( val );
                    array.add( val );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        handler.endArray( array );
        return array;
    }

}
