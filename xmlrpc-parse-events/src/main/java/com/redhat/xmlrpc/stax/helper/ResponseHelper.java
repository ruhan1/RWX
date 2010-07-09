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
import com.redhat.xmlrpc.raw.model.XmlRpcFault;
import com.redhat.xmlrpc.raw.model.XmlRpcParameter;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.ArrayList;
import java.util.List;

public class ResponseHelper
    implements StaxHelper<XmlRpcResponse>
{

    private static final FaultHelper FAULT_HELPER = new FaultHelper();

    private static final ParamHelper PARAM_PARSER = new ParamHelper();

    @Override
    public XmlRpcResponse parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        handler.startResponse();

        final List<XmlRpcParameter<?>> params = new ArrayList<XmlRpcParameter<?>>();
        XmlRpcFault fault = null;

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.FAULT.equals( tag ) )
                {
                    fault = FAULT_HELPER.parse( reader, null );
                }
                else if ( XmlRpcConstants.PARAMS.equals( tag ) )
                {
                    params.add( PARAM_PARSER.parse( reader, null ) );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        final XmlRpcResponse resp;
        if ( fault == null )
        {
            resp = new XmlRpcResponse();
            resp.setParameters( params );
        }
        else
        {
            resp = new XmlRpcResponse( fault );
        }

        handler.endResponse( resp );
        return resp;
    }

}
