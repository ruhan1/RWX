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

import java.util.Collections;
import java.util.Map;

public abstract class AbstractValueConverter
    extends org.commonjava.rwx.spi.AbstractXmlRpcListener
    implements ValueConverter
{

    private XmlRpcListener parent;

    private Map<Class<?>, Mapping<?>> recipes;

    @Override
    public Map<Class<?>, Mapping<?>> getSupplementalRecipes( final Mapper loader )
        throws BindException
    {
        return Collections.emptyMap();
    }

    @Override
    public final void setContext( final XmlRpcListener parent, final Map<Class<?>, Mapping<?>> recipes )
        throws XmlRpcException
    {
        this.parent = parent;
        this.recipes = recipes;
    }

    protected final XmlRpcListener getParentListener()
    {
        return parent;
    }

    protected final Map<Class<?>, Mapping<?>> getRecipes()
    {
        return recipes;
    }
}
