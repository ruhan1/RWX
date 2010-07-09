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
    implements Iterable<XmlRpcParameter<?>>
{

    private final XmlRpcFault fault;

    private final List<XmlRpcParameter<?>> params = new ArrayList<XmlRpcParameter<?>>();

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

    public void addParameter( final XmlRpcParameter<?> param )
    {
        params.add( param );
    }

    public void setParameters( final List<XmlRpcParameter<?>> params )
    {
        this.params.clear();
        if ( !params.isEmpty() )
        {
            this.params.addAll( params );
        }
    }

    public List<XmlRpcParameter<?>> getParameters()
    {
        return params;
    }

    @Override
    public Iterator<XmlRpcParameter<?>> iterator()
    {
        return params.iterator();
    }

}
