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

public interface XmlRpcListener
{

    XmlRpcListener value( Object value, ValueType type )
        throws XmlRpcException;

    XmlRpcListener fault( int code, String message )
        throws XmlRpcException;

    XmlRpcListener startRequest()
        throws XmlRpcException;

    XmlRpcListener requestMethod( String methodName )
        throws XmlRpcException;

    XmlRpcListener endRequest()
        throws XmlRpcException;

    XmlRpcListener startResponse()
        throws XmlRpcException;

    XmlRpcListener endResponse()
        throws XmlRpcException;

    XmlRpcListener startParameter( int index )
        throws XmlRpcException;

    XmlRpcListener endParameter()
        throws XmlRpcException;

    XmlRpcListener parameter( int index, Object value, ValueType type )
        throws XmlRpcException;

    XmlRpcListener startArray()
        throws XmlRpcException;

    XmlRpcListener startArrayElement( int index )
        throws XmlRpcException;

    XmlRpcListener endArrayElement()
        throws XmlRpcException;

    XmlRpcListener arrayElement( int index, Object value, ValueType type )
        throws XmlRpcException;

    XmlRpcListener endArray()
        throws XmlRpcException;

    XmlRpcListener startStruct()
        throws XmlRpcException;

    XmlRpcListener startStructMember( String key )
        throws XmlRpcException;

    XmlRpcListener endStructMember()
        throws XmlRpcException;

    XmlRpcListener structMember( String key, Object value, ValueType type )
        throws XmlRpcException;

    XmlRpcListener endStruct()
        throws XmlRpcException;

}
