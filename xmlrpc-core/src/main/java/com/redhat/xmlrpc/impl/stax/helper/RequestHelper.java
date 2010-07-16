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
import com.redhat.xmlrpc.vocab.XmlRpcConstants;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class RequestHelper
    implements XMLStreamConstants
{

    public static void parse( final XMLStreamReader reader, final XmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        listener.startRequest();

        boolean methodGiven = false;
        int type = -1;
        do
        {
            type = reader.nextTag();
            if ( type == START_ELEMENT )
            {
                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.METHOD_NAME.equals( tag ) )
                {
                    if ( methodGiven )
                    {
                        throw new XmlRpcException( "Invalid request. Multiple methodName parameters." );
                    }

                    listener.requestMethod( reader.getElementText().trim() );
                    methodGiven = true;
                }
                else if ( XmlRpcConstants.PARAMS.equals( tag ) )
                {
                    ParamHelper.parse( reader, listener );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT
                && XmlRpcConstants.REQUEST.equals( reader.getName().getLocalPart() ) )
            {
                break;
            }
        }
        while ( type != END_DOCUMENT );

        if ( !methodGiven )
        {
            throw new XmlRpcException( "methodName must be specified!" );
        }

        listener.endRequest();
    }

}
