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

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.ArrayList;
import java.util.List;

public class ArrayHelper
    implements XMLStreamConstants
{

    public static List<Object> parse( final XMLStreamReader reader, final XmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        return parse( reader, listener, true );
    }

    public static List<Object> parse( final XMLStreamReader reader, final XmlRpcListener listener,
                                      final boolean enableEvents )
        throws XMLStreamException, XmlRpcException
    {
        if ( enableEvents )
        {
            listener.startArray();
        }

        final List<Object> values = new ArrayList<Object>();

        int count = 0;
        int type = -1;
        do
        {
            type = reader.nextTag();
            if ( type == START_ELEMENT )
            {
                if ( XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
                {
                    if ( enableEvents )
                    {
                        listener.startArrayElement( count );
                    }

                    final ValueHelper vh = new ValueHelper();
                    vh.parse( reader, listener );

                    final Object value = vh.getValue();
                    final ValueType vt = vh.getValueType();

                    values.add( value );

                    if ( enableEvents )
                    {
                        listener.arrayElement( count, value, vt );
                        listener.endArrayElement();
                    }

                    count++;
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT
                && XmlRpcConstants.ARRAY.equals( reader.getName().getLocalPart() ) )
            {
                break;
            }
        }
        while ( type != END_DOCUMENT );

        if ( enableEvents )
        {
            listener.endArray();
        }

        return values;
    }

}
