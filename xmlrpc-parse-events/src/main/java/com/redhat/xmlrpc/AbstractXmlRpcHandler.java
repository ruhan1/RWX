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

package com.redhat.xmlrpc;

import com.redhat.xmlrpc.raw.model.XmlRpcArray;
import com.redhat.xmlrpc.raw.model.XmlRpcFault;
import com.redhat.xmlrpc.raw.model.XmlRpcRequest;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;
import com.redhat.xmlrpc.raw.model.XmlRpcStruct;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

public abstract class AbstractXmlRpcHandler
    implements XmlRpcHandler
{

    protected AbstractXmlRpcHandler()
    {
    }

    @Override
    public void endArray( final XmlRpcArray array )
    {
    }

    @Override
    public void endRequest( final XmlRpcRequest request )
    {
    }

    @Override
    public void endResponse( final XmlRpcResponse response )
    {
    }

    @Override
    public void endStruct( final XmlRpcStruct struct )
    {
    }

    @Override
    public void fault( final XmlRpcFault fault )
    {
    }

    @Override
    public void startArray()
    {
    }

    @Override
    public void startResponse()
    {
    }

    @Override
    public void startStruct()
    {
    }

    @Override
    public void arrayElement( final XmlRpcValue value )
    {
    }

    @Override
    public void parameter( final XmlRpcValue value )
    {
    }

    @Override
    public void requestMethodName( final String methodName )
    {
    }

    @Override
    public void startRequest()
    {
    }

    @Override
    public void structMember( final String key, final XmlRpcValue value )
    {
    }

}
