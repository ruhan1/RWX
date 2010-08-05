/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
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
