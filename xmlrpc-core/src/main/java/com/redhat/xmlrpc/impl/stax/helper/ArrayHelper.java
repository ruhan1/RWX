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

import java.util.ArrayList;
import java.util.List;

public class ArrayHelper
{

    private static final ValueHelper VALUE_PARSER = new ValueHelper();

    public List<Object> parse( final XMLStreamReader reader, final XmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        listener.startArray();

        final List<Object> values = new ArrayList<Object>();

        int level = 1;
        int count = 0;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                if ( XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
                {
                    listener.startArrayElement( count );

                    final ValueType vt = VALUE_PARSER.typeOf( reader );
                    final Object value = VALUE_PARSER.valueOf( reader, vt, listener );

                    values.add( value );
                    listener.arrayElement( count, value, vt );
                    listener.endArrayElement();
                }

                count++;
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        listener.endArray();

        return values;
    }

}
