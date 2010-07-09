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

package com.redhat.xmlrpc.raw.error;

import com.redhat.xmlrpc.raw.model.XmlRpcFault;

public class XmlRpcFaultException
    extends XmlRpcException
{

    private static final long serialVersionUID = 1L;

    private final XmlRpcFault fault;

    public XmlRpcFaultException( final XmlRpcFault fault )
    {
        super( fault.getCode() + ": " + fault.getMessage() );
        this.fault = fault;
    }

    public int getCode()
    {
        return fault.getCode();
    }

    public String getReason()
    {
        return fault.getMessage();
    }

}
