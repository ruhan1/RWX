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

package com.redhat.xmlrpc.binding.spi;

import com.redhat.xmlrpc.binding.BinderyContext;
import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

public interface ValueConverter
{

    XmlRpcValue<?> render( Object src, BinderyContext context )
        throws BindException;

    Object parse( XmlRpcValue<?> src, BinderyContext context )
        throws BindException;

}
