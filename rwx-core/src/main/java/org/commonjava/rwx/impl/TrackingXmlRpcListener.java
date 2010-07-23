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
