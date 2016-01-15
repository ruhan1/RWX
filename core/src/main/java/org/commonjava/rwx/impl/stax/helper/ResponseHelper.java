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

public class ResponseHelper
    implements XMLStreamConstants
{

    public static void parse( final XMLStreamReader reader, final TrackingXmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        listener.startResponse();

        boolean paramDetected = false;
        boolean faultDetected = false;

        int type = -1;
        do
        {
            type = reader.nextTag();
            if ( type == START_ELEMENT )
            {
                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.FAULT.equals( tag ) )
                {
                    faultDetected = true;
                    FaultHelper.parse( reader, listener );
                    break;
                }
                else if ( XmlRpcConstants.PARAMS.equals( tag ) )
                {
                    paramDetected = true;
                    ParamHelper.parse( reader, listener );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT
                && XmlRpcConstants.RESPONSE.equals( reader.getName().getLocalPart() ) )
            {
                break;
            }
        }
        while ( type != END_DOCUMENT );

        if ( paramDetected && faultDetected )
        {
            throw new XmlRpcException( "Cannot specify both fault and parameters in XML-RPC response." );
        }

        listener.endResponse();
    }

}
