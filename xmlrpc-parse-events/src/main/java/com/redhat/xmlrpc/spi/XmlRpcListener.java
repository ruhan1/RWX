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

    void fault( int code, String message )
        throws XmlRpcException;

    void startRequest()
        throws XmlRpcException;

    void requestMethod( String methodName )
        throws XmlRpcException;

    void endRequest()
        throws XmlRpcException;

    void startResponse()
        throws XmlRpcException;

    void endResponse()
        throws XmlRpcException;

    void startComplexParameter( int index )
        throws XmlRpcException;

    void endComplexParameter()
        throws XmlRpcException;

    void parameter( int index, Object value, ValueType type )
        throws XmlRpcException;

    void startArray()
        throws XmlRpcException;

    void arrayElement( int index, Object value, ValueType type )
        throws XmlRpcException;

    void endArray()
        throws XmlRpcException;

    void startStruct()
        throws XmlRpcException;

    void structMember( String key, Object value, ValueType type )
        throws XmlRpcException;

    void endStruct()
        throws XmlRpcException;

}
