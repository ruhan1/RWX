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
    public void arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
    }

    @Override
    public void endArray()
        throws XmlRpcException
    {
    }

    @Override
    public void endRequest()
        throws XmlRpcException
    {
    }

    @Override
    public void endResponse()
        throws XmlRpcException
    {
    }

    @Override
    public void endStruct()
        throws XmlRpcException
    {
    }

    @Override
    public void fault( final int code, final String message )
        throws XmlRpcException
    {
    }

    @Override
    public void startComplexParameter( final int index )
        throws XmlRpcException
    {
    }

    @Override
    public void endComplexParameter()
        throws XmlRpcException
    {
    }

    @Override
    public void parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
    }

    @Override
    public void requestMethod( final String methodName )
        throws XmlRpcException
    {
    }

    @Override
    public void startArray()
        throws XmlRpcException
    {
    }

    @Override
    public void startRequest()
        throws XmlRpcException
    {
    }

    @Override
    public void startResponse()
        throws XmlRpcException
    {
    }

    @Override
    public void startStruct()
        throws XmlRpcException
    {
    }

    @Override
    public void structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
    }

}
