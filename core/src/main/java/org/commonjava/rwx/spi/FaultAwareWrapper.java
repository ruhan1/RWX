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
package org.commonjava.rwx.spi;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.vocab.ValueType;

public class FaultAwareWrapper
    implements XmlRpcListener
{

    private final XmlRpcListener listener;

    public FaultAwareWrapper( final XmlRpcListener listener )
    {
        this.listener = listener;
    }

    public XmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        return listener.value( value, type );
    }

    public XmlRpcListener fault( final int code, final String message )
        throws XmlRpcException
    {
        listener.fault( code, message );
        throw new XmlRpcFaultException( code, message );
    }

    public XmlRpcListener startRequest()
        throws XmlRpcException
    {
        return listener.startRequest();
    }

    public XmlRpcListener requestMethod( final String methodName )
        throws XmlRpcException
    {
        return listener.requestMethod( methodName );
    }

    public XmlRpcListener endRequest()
        throws XmlRpcException
    {
        return listener.endRequest();
    }

    public XmlRpcListener startResponse()
        throws XmlRpcException
    {
        return listener.startResponse();
    }

    public XmlRpcListener endResponse()
        throws XmlRpcException
    {
        return listener.endResponse();
    }

    public XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        return listener.startParameter( index );
    }

    public XmlRpcListener endParameter()
        throws XmlRpcException
    {
        return listener.endParameter();
    }

    public XmlRpcListener parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return listener.parameter( index, value, type );
    }

    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        return listener.startArray();
    }

    public XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        return listener.startArrayElement( index );
    }

    public XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        return listener.endArrayElement();
    }

    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return listener.arrayElement( index, value, type );
    }

    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        return listener.endArray();
    }

    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        return listener.startStruct();
    }

    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        return listener.startStructMember( key );
    }

    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        return listener.endStructMember();
    }

    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return listener.structMember( key, value, type );
    }

    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        return listener.endStruct();
    }

}
