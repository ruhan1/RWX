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

package com.redhat.xmlrpc.raw.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlRpcResponse
    implements Iterable<XmlRpcValue<?>>, XmlRpcMessage
{

    private final XmlRpcFault fault;

    private final List<XmlRpcValue<?>> params = new ArrayList<XmlRpcValue<?>>();

    public XmlRpcResponse()
    {
        fault = null;
    }

    public XmlRpcResponse( final XmlRpcFault fault )
    {
        this.fault = fault;
    }

    public XmlRpcFault getFault()
    {
        return fault;
    }

    public void addParameter( final XmlRpcValue<?> param )
    {
        params.add( param );
    }

    public void setParameters( final List<XmlRpcValue<?>> params )
    {
        this.params.clear();
        if ( !params.isEmpty() )
        {
            this.params.addAll( params );
        }
    }

    public List<XmlRpcValue<?>> getParameters()
    {
        return params;
    }

    @Override
    public Iterator<XmlRpcValue<?>> iterator()
    {
        return params.iterator();
    }

}
