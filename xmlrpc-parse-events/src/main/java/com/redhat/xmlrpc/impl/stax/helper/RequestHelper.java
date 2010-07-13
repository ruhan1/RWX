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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class RequestHelper
    implements StaxHelper
{

    private static final ParamHelper PARAM_HELPER = new ParamHelper();

    @Override
    public void parse( final XMLStreamReader reader, final XmlRpcListener listener )
        throws XMLStreamException, XmlRpcException
    {
        listener.startRequest();

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.METHOD_NAME.equals( tag ) )
                {
                    listener.requestMethod( reader.getElementText().trim() );
                }
                else if ( XmlRpcConstants.PARAM.equals( tag ) )
                {
                    PARAM_HELPER.parse( reader, listener );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        listener.endRequest();
    }

}
