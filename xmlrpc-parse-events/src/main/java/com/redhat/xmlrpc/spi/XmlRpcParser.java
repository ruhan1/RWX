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

package com.redhat.xmlrpc.spi;

import com.redhat.xmlrpc.XmlRpcHandler;
import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcRequest;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;

import java.io.InputStream;
import java.io.Reader;

public interface XmlRpcParser
{

    XmlRpcResponse parseResponse( InputStream stream, XmlRpcHandler handler )
        throws XmlRpcException;

    XmlRpcResponse parseResponse( Reader reader, XmlRpcHandler handler )
        throws XmlRpcException;

    XmlRpcResponse parseResponse( String xml, XmlRpcHandler handler )
        throws XmlRpcException;

    XmlRpcResponse parseResponse( InputStream stream )
        throws XmlRpcException;

    XmlRpcResponse parseResponse( Reader reader )
        throws XmlRpcException;

    XmlRpcResponse parseResponse( String xml )
        throws XmlRpcException;

    XmlRpcRequest parseRequest( InputStream stream, XmlRpcHandler handler )
        throws XmlRpcException;

    XmlRpcRequest parseRequest( Reader reader, XmlRpcHandler handler )
        throws XmlRpcException;

    XmlRpcRequest parseRequest( String xml, XmlRpcHandler handler )
        throws XmlRpcException;

    XmlRpcRequest parseRequest( InputStream stream )
        throws XmlRpcException;

    XmlRpcRequest parseRequest( Reader reader )
        throws XmlRpcException;

    XmlRpcRequest parseRequest( String xml )
        throws XmlRpcException;

}
