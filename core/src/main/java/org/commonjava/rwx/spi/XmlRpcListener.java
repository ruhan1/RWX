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
package org.commonjava.rwx.spi;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.vocab.ValueType;

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
