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
import org.commonjava.rwx.vocab.XmlRpcConstants;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.Map;

public class FaultHelper
    implements XMLStreamConstants
{

    @SuppressWarnings( "unchecked" )
    public static void parse( final XMLStreamReader reader, final TrackingXmlRpcListener handler )
        throws XMLStreamException, XmlRpcException
    {
        Map<String, Object> values = null;

        int type = -1;
        do
        {
            type = reader.nextTag();
            if ( type == START_ELEMENT )
            {
                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.VALUE.equals( tag ) )
                {
                    if ( values != null )
                    {
                        throw new XmlRpcException( "Fault can only contain ONE value." );
                    }

                    final ValueHelper vh = new ValueHelper( false );
                    values = (Map<String, Object>) vh.parse( reader, handler );
                    break;
                }
                else
                {
                    throw new XmlRpcException( "Invalid nested element within fault: " + tag );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT
                && XmlRpcConstants.REQUEST.equals( reader.getName().getLocalPart() ) )
            {
                break;
            }
        }
        while ( type != END_DOCUMENT );

        if ( values == null )
        {
            throw new XmlRpcException( "Invalid fault. No code or string information provided!" );
        }

        handler.fault( (Integer) values.get( XmlRpcConstants.FAULT_CODE ),
                       (String) values.get( XmlRpcConstants.FAULT_STRING ) );
    }

}
