/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.rwx.impl.stax.helper;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx.vocab.XmlRpcConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class StructHelper
    implements XMLStreamConstants
{

    public static Map<String, Object> parse( final XMLStreamReader reader, final TrackingXmlRpcListener handler )
        throws XMLStreamException, XmlRpcException
    {
        return parse( reader, handler, true );
    }

    public static Map<String, Object> parse( final XMLStreamReader reader, final TrackingXmlRpcListener handler,
                                             final boolean enableEvents )
        throws XMLStreamException, XmlRpcException
    {
        if ( enableEvents )
        {
            handler.startStruct();
        }

        final Map<String, Object> values = new LinkedHashMap<>();

        int type;
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

    private static void parseMember( final XMLStreamReader reader, final TrackingXmlRpcListener handler,
                                     final Map<String, Object> values, final boolean enableEvents )
        throws XMLStreamException, XmlRpcException
    {
        String name = null;
        Object value;
        ValueType vt;

        if ( reader.nextTag() == START_ELEMENT && XmlRpcConstants.NAME.equals( reader.getName().getLocalPart() ) )
        {
            name = reader.getElementText().trim();
            if ( enableEvents )
            {
                handler.startStructMember( name );
            }
        }

        if ( name == null )
        {
            throw new XmlRpcException( "Invalid struct member. Name is missing. Location: "
                + reader.getLocation().getLineNumber() + ":" + reader.getLocation().getColumnNumber() );
        }

        if ( reader.nextTag() == START_ELEMENT && XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
        {
            Logger logger = LoggerFactory.getLogger( StructHelper.class );
            logger.trace( "Handing off to ValueHelper at: {}", reader.getLocation() );
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
    }

}
