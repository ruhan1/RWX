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

    private static final Logger LOGGER = Logger.getLogger( ValueHelper.class );

    private Object value;

    private boolean valueSet = false;

    private ValueType type;

    private boolean enableEvents = false;

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
            evt = reader.next();
            if ( !valueSet && evt == CHARACTERS )
            {
                final String src = reader.getText();

                type = ValueType.STRING;
                value = src;
            }
            else if ( evt == START_ELEMENT )
            {
                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.VALUE.equals( tag ) )
                {
                    throw new XmlRpcException( "Nested " + XmlRpcConstants.VALUE + " elements detected." );
                }
                else if ( XmlRpcConstants.STRUCT.equals( tag ) )
                {
                    trace( LOGGER, "Handing off to StructHelper at: $1", reader.getLocation() );
                    value = StructHelper.parse( reader, listener, enableEvents );
                    type = ValueType.STRUCT;
                    valueSet = true;
                }
                else if ( XmlRpcConstants.ARRAY.equals( tag ) )
                {
                    trace( LOGGER, "Handing off to ArrayHelper at: $1", reader.getLocation() );
                    value = ArrayHelper.parse( reader, listener, enableEvents );
                    type = ValueType.ARRAY;
                    valueSet = true;
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
        type = ValueType.typeOf( reader.getName().getLocalPart() );

        final String src = reader.getElementText().trim();

        value = type == null ? src : type.coercion().fromString( src );
        valueSet = true;
    }

}
