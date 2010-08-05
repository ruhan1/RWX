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
