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

package org.commonjava.rwx.binding.internal.xbr.helper;

import org.commonjava.rwx.spi.AbstractXmlRpcListener;

public class AbstractBinder
    extends AbstractXmlRpcListener
    implements Binder
{

    private final Class<?> type;

    private final XBRBindingContext context;

    private final Binder parent;

    protected AbstractBinder( final Binder parent, final Class<?> type, final XBRBindingContext context )
    {
        this.parent = parent;
        this.context = context;
        this.type = type;
    }

    protected final Binder getParent()
    {
        return parent;
    }

    protected final XBRBindingContext getContext()
    {
        return context;
    }

    protected final Class<?> getType()
    {
        return type;
    }

}
