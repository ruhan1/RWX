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
import com.redhat.xmlrpc.raw.model.XmlRpcStruct;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.Map;

public class FaultHelper
    implements StaxHelper<XmlRpcFault>
{

    private static final StructHelper STRUCT_HELPER = new StructHelper();

    @Override
    public XmlRpcFault parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        XmlRpcStruct struct = null;

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.STRUCT.equals( tag ) )
                {
                    struct = STRUCT_HELPER.parse( reader, null );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        if ( struct == null )
        {
            throw new XmlRpcException( "Invalid XML-RPC fault. No fault information was provided." );
        }

        final Map<String, Object> values = struct.rawStruct();
        final Integer code = (Integer) values.get( XmlRpcConstants.FAULT_CODE );
        final String message = (String) values.get( XmlRpcConstants.FAULT_STRING );

        final XmlRpcFault fault = new XmlRpcFault( code, message );

        handler.fault( fault );
        return fault;
    }

}
