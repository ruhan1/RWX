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

package org.commonjava.rwx.binding.spi.value;

import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcListener;

public abstract class AbstractSimpleValueBinder
    extends AbstractXmlRpcListener
    implements ValueBinder
{

    private static final String MESSAGE = "Not supported in simple binder: ";

    private final Class<?> type;

    private final BindingContext context;

    private final Binder parent;

    protected AbstractSimpleValueBinder( final Binder parent, final Class<?> type, final BindingContext context )
    {
        this.parent = parent;
        this.context = context;
        this.type = type;
    }

    public final Binder getParent()
    {
        return parent;
    }

    public final BindingContext getBindingContext()
    {
        return context;
    }

    public final Class<?> getType()
    {
        return type;
    }

    @Override
    public final XmlRpcListener endArray()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endStruct()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startArray()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startStruct()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endParameter()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        throw forbidden();
    }

    private XmlRpcException forbidden()
    {
        return new XmlRpcException( MESSAGE + getClass().getName() );
    }

}
