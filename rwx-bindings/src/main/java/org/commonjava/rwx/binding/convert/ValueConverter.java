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

package org.commonjava.rwx.binding.convert;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.discovery.Mapper;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;

import java.util.Map;

public interface ValueConverter
    extends XmlRpcListener
{

    Map<Class<?>, Mapping<?>> getSupplementalRecipes( Mapper loader )
        throws BindException;

    void generate( XmlRpcListener listener, Object value, Map<Class<?>, Mapping<?>> recipes )
        throws XmlRpcException;

    void setContext( XmlRpcListener parent, Map<Class<?>, Mapping<?>> recipes )
        throws XmlRpcException;

}
