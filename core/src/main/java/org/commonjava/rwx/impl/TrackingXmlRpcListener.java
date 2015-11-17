/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.impl;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

public class TrackingXmlRpcListener
    implements XmlRpcListener
{

    private XmlRpcListener current;

    private final XmlRpcListener root;

    public TrackingXmlRpcListener( final XmlRpcListener root )
    {
        this.root = root;
        current = root;
    }

    public XmlRpcListener getRoot()
    {
        return root;
    }

    public XmlRpcListener getCurrent()
    {
        return current;
    }

    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return current = current.arrayElement( index, value, type );
    }

    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        return current = current.endArray();
    }

    public XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        return current = current.endArrayElement();
    }

    public XmlRpcListener endParameter()
        throws XmlRpcException
    {
        return current = current.endParameter();
    }

    public XmlRpcListener endRequest()
        throws XmlRpcException
    {
        return current = current.endRequest();
    }

    public XmlRpcListener endResponse()
        throws XmlRpcException
    {
        return current = current.endResponse();
    }

    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        return current = current.endStruct();
    }

    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        return current = current.endStructMember();
    }

    public XmlRpcListener fault( final int code, final String message )
        throws XmlRpcException
    {
        return current = current.fault( code, message );
    }

    public XmlRpcListener parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return current = current.parameter( index, value, type );
    }

    public XmlRpcListener requestMethod( final String methodName )
        throws XmlRpcException
    {
        return current = current.requestMethod( methodName );
    }

    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        return current = current.startArray();
    }

    public XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        return current = current.startArrayElement( index );
    }

    public XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        return current = current.startParameter( index );
    }

    public XmlRpcListener startRequest()
        throws XmlRpcException
    {
        return current = current.startRequest();
    }

    public XmlRpcListener startResponse()
        throws XmlRpcException
    {
        return current = current.startResponse();
    }

    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        return current = current.startStruct();
    }

    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        return current = current.startStructMember( key );
    }

    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return current = current.structMember( key, value, type );
    }

    public XmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        return current = current.value( value, type );
    }

}
