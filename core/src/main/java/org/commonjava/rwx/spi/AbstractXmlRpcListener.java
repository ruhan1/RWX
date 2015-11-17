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
import org.commonjava.rwx.vocab.ValueType;

public abstract class AbstractXmlRpcListener
    implements XmlRpcListener
{

    protected AbstractXmlRpcListener()
    {
    }

    @Override
    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endRequest()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endResponse()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener fault( final int code, final String message )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endParameter()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener requestMethod( final String methodName )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startRequest()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startResponse()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public XmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

}
