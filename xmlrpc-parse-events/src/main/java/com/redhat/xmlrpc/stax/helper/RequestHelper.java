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

package com.redhat.xmlrpc.stax.helper;

import com.redhat.xmlrpc.XmlRpcHandler;
import com.redhat.xmlrpc.raw.XmlRpcConstants;
import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcParameter;
import com.redhat.xmlrpc.raw.model.XmlRpcRequest;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.ArrayList;
import java.util.List;

public class RequestHelper
    implements StaxHelper<XmlRpcRequest>
{

    public XmlRpcRequest parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        handler.startRequest();

        String methodName = null;
        final List<XmlRpcParameter<?>> params = new ArrayList<XmlRpcParameter<?>>();

        final ParamHelper pparser = new ParamHelper();

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
                    methodName = reader.getElementText().trim();
                    handler.requestMethodName( methodName );
                }
                else if ( XmlRpcConstants.PARAMS.equals( tag ) )
                {
                    params.add( pparser.parse( reader, null ) );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        if ( methodName == null )
        {
            throw new XmlRpcException( "Invalid method call: method name is missing." );
        }

        final XmlRpcRequest req = new XmlRpcRequest( methodName );
        req.setParameters( params );

        handler.endRequest( req );
        return req;
    }

}
