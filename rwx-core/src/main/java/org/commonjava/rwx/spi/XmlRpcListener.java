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
