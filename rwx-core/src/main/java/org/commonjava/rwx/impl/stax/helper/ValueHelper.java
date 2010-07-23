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

package org.commonjava.rwx.impl.stax.helper;

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx.vocab.XmlRpcConstants;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * NOTE: This is a STATEFUL helper. It is NOT threadsafe!
 */
public class ValueHelper
    implements XMLStreamConstants
{

    private Object value;

    private ValueType type;

    private final boolean enableEvents;

    public ValueHelper()
    {
        enableEvents = true;
    }

    public ValueHelper( final boolean enableEvents )
    {
        this.enableEvents = enableEvents;
    }

    public Object getValue()
    {
        return value;
    }

    public ValueType getValueType()
    {
        return type;
    }

    public Object parse( final XMLStreamReader reader, final TrackingXmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        int evt = -1;
        do
        {
            evt = reader.nextTag();
            if ( evt == START_ELEMENT )
            {
                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.VALUE.equals( tag ) )
                {
                    throw new XmlRpcException( "Nested " + XmlRpcConstants.VALUE + " elements detected." );
                }
                else if ( XmlRpcConstants.STRUCT.equals( tag ) )
                {
                    value = StructHelper.parse( reader, listener, enableEvents );
                    type = ValueType.STRUCT;
                }
                else if ( XmlRpcConstants.ARRAY.equals( tag ) )
                {
                    value = ArrayHelper.parse( reader, listener, enableEvents );
                    type = ValueType.ARRAY;
                }
                else
                {
                    parseSimpleValue( reader, listener );
                }
            }
            else if ( evt == XMLStreamReader.END_ELEMENT
                && XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
            {
                break;
            }
        }
        while ( evt != END_DOCUMENT );

        if ( enableEvents )
        {
            listener.value( value, type );
        }
        return value;
    }

    private void parseSimpleValue( final XMLStreamReader reader, final TrackingXmlRpcListener listener )
        throws XMLStreamException, CoercionException
    {
        //        while ( reader.hasNext() && reader.next() != XMLStreamReader.START_ELEMENT )
        //        {
        //            // NOP
        //        }

        type = ValueType.typeOf( reader.getName().getLocalPart() );

        final String src = reader.getElementText().trim();

        value = type == null ? src : type.coercion().fromString( src );
    }

}
