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
    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endParameter()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        throw forbidden();
    }

    private XmlRpcException forbidden()
    {
        return new XmlRpcException( MESSAGE + getClass().getName() );
    }

}
