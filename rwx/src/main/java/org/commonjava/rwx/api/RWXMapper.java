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
package org.commonjava.rwx.api;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.core.Parser;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.core.Renderer;
import org.commonjava.rwx.core.XmlRpcParser;
import org.commonjava.rwx.model.Fault;
import org.commonjava.rwx.model.RpcObject;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

import static org.commonjava.rwx.util.RenderUtils.toXMLString;

/**
 * Created by ruhan on 7/12/17.
 */
public final class RWXMapper
{
    /**
     * Render an object to XML-RPC request or response string.
     *
     * @param obj
     * @return
     * @throws XmlRpcException
     */
    public String render( Object obj ) throws XmlRpcException
    {
        Object rpcObject = Registry.getInstance().renderTo( obj );
        return toXMLString( rpcObject );
    }

    /**
     * Parse a XML-RPC request or response stream (XML string) to an object.
     *
     * @param stream
     * @param type
     * @param <T>
     * @return
     * @throws XmlRpcException
     */
    public <T> T parse( InputStream stream, Class<T> type ) throws XmlRpcException
    {
        final XmlRpcParser xmlRpcParser = new XmlRpcParser( stream );
        RpcObject rpcObject = null;
        try
        {
            rpcObject = xmlRpcParser.parse();
        }
        catch ( XMLStreamException e )
        {
            throw new XmlRpcException( "Parse to RpcObject failed", e );
        }

        if ( rpcObject instanceof Fault )
        {
            throw new XmlRpcFaultException( (Fault) rpcObject );
        }

        return Registry.getInstance().parseAs( rpcObject, type );
    }
}
