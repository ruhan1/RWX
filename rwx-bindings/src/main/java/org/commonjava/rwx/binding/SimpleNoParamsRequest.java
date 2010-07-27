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

package org.commonjava.rwx.binding;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;

public class SimpleNoParamsRequest
    implements XmlRpcGenerator
{

    private final String methodName;

    public SimpleNoParamsRequest( final String methodName )
    {
        this.methodName = methodName;
    }

    @Override
    public XmlRpcGenerator generate( final XmlRpcListener listener )
        throws XmlRpcException
    {
        listener.startRequest().requestMethod( methodName ).endRequest();
        return this;
    }

    public String getMethodName()
    {
        return methodName;
    }

}
