/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
 */

package org.commonjava.rwx.impl.stax.helper;

import static org.commonjava.rwx.util.LogUtil.trace;

import org.apache.log4j.Logger;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx.vocab.XmlRpcConstants;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.LinkedHashMap;
import java.util.Map;

public class StructHelper
    implements XMLStreamConstants
{

    private static final Logger LOGGER = Logger.getLogger( StructHelper.class );

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

    private static void parseMember( final XMLStreamReader reader, final TrackingXmlRpcListener handler,
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

        if ( name == null )
        {
            throw new XmlRpcException( "Invalid struct member. Name is missing. Location: "
                + reader.getLocation().getLineNumber() + ":" + reader.getLocation().getColumnNumber() );
        }

        if ( reader.nextTag() == START_ELEMENT && XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
        {
            trace( LOGGER, "Handing off to ValueHelper at: $1", reader.getLocation() );
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
