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

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.vocab.ValueType;

public abstract class AbstractXmlRpcListener
    implements XmlRpcListener
{

    protected AbstractXmlRpcListener()
    {
    }

    @Override
    public AbstractXmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endArray()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endRequest()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endResponse()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endStruct()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener fault( final int code, final String message )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endParameter()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener requestMethod( final String methodName )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startArray()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startRequest()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startResponse()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startStruct()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener endStructMember()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public AbstractXmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

}
