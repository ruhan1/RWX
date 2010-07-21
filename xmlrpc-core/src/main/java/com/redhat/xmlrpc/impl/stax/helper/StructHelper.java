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

import java.util.LinkedHashMap;
import java.util.Map;

public class StructHelper
    implements XMLStreamConstants
{

    public static Map<String, Object> parse( final XMLStreamReader reader, final XmlRpcListener handler )
        throws XMLStreamException, XmlRpcException
    {
        return parse( reader, handler, true );
    }

    public static Map<String, Object> parse( final XMLStreamReader reader, final XmlRpcListener handler,
                                             final boolean enableEvents )
        throws XMLStreamException, XmlRpcException
    {
        if ( enableEvents )
        {
            handler.startStruct();
        }

        final Map<String, Object> values = new LinkedHashMap<String, Object>();

        int type = -1;
        do
        {
            type = reader.nextTag();
            if ( type == START_ELEMENT )
            {
                if ( XmlRpcConstants.MEMBER.equals( reader.getName().getLocalPart() ) )
                {
                    parseMember( reader, handler, values, enableEvents );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT
                && XmlRpcConstants.STRUCT.equals( reader.getName().getLocalPart() ) )
            {
                break;
            }
        }
        while ( type != END_DOCUMENT );

        if ( enableEvents )
        {
            handler.endStruct();
        }

        return values;
    }

    private static void parseMember( final XMLStreamReader reader, final XmlRpcListener handler,
                                     final Map<String, Object> values, final boolean enableEvents )
        throws XMLStreamException, XmlRpcException
    {
        String name = null;
        Object value = null;
        ValueType vt = null;

        if ( reader.nextTag() == START_ELEMENT && XmlRpcConstants.NAME.equals( reader.getName().getLocalPart() ) )
        {
            name = reader.getElementText().trim();
            if ( enableEvents )
            {
                handler.startStructMember( name );
            }
        }

        if ( reader.nextTag() == START_ELEMENT && XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
        {
            final ValueHelper vh = new ValueHelper( enableEvents );
            vh.parse( reader, handler );

            value = vh.getValue();
            vt = vh.getValueType();

            values.put( name, value );
            if ( enableEvents )
            {
                handler.structMember( name, value, vt );
                handler.endStructMember();
            }
        }

        if ( name == null )
        {
            throw new XmlRpcException( "Invalid struct member. Name is missing. Location: "
                + reader.getLocation().getLineNumber() + ":" + reader.getLocation().getColumnNumber() );
        }
        else if ( ValueType.NIL != vt && value == null )
        {
            throw new XmlRpcException( "Invalid struct member. Value is missing. Location: "
                + reader.getLocation().getLineNumber() + ":" + reader.getLocation().getColumnNumber() );
        }
    }

}
