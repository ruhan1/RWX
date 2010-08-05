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

package org.commonjava.rwx.binding.internal.xbr.helper;

import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

public abstract class AbstractBinder
    extends AbstractXmlRpcListener
    implements Binder
{

    private final Class<?> type;

    private Object value;

    private final BindingContext context;

    private final Binder parent;

    private int count = 0;

    private ValueType valueType;

    protected AbstractBinder( final Binder parent, final Class<?> type, final BindingContext context )
    {
        this.parent = parent;
        this.context = context;
        this.type = type;
    }

    protected final void setValue( final Object value, final ValueType valueType )
    {
        this.value = value;
        this.valueType = valueType;
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
        return decrement( endArrayInternal() );
    }

    protected Binder endArrayInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        return decrement( endArrayElementInternal() );
    }

    protected Binder endArrayElementInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener endStruct()
        throws XmlRpcException
    {
        return decrement( endStructInternal() );
    }

    protected Binder endStructInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        return decrement( endStructMemberInternal() );
    }

    protected Binder endStructMemberInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener startArray()
        throws XmlRpcException
    {
        return increment( startArrayInternal() );
    }

    protected Binder startArrayInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        return increment( startArrayElementInternal( index ) );
    }

    protected Binder startArrayElementInternal( final int index )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener startStruct()
        throws XmlRpcException
    {
        return increment( startStructInternal() );
    }

    protected Binder startStructInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        return increment( startStructMemberInternal( key ) );
    }

    protected Binder startStructMemberInternal( final String key )
        throws XmlRpcException
    {
        return this;
    }

    private final Binder decrement( final Binder binder )
    {
        count--;

        return binder;
    }

    private final Binder increment( final Binder binder )
    {
        count++;
        return binder;
    }

    @Override
    public final XmlRpcListener value( final Object v, final ValueType t )
        throws XmlRpcException
    {
        if ( count < 1 )
        {
            parent.value( value, valueType );
            return parent;
        }
        else
        {
            return valueInternal( v, t );
        }
    }

    protected Binder valueInternal( final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener endParameter()
        throws XmlRpcException
    {
        return decrement( endParameterInternal() );
    }

    protected Binder endParameterInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    public final XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        return increment( startParameterInternal( index ) );
    }

    protected Binder startParameterInternal( final int index )
        throws XmlRpcException
    {
        return this;
    }

}
