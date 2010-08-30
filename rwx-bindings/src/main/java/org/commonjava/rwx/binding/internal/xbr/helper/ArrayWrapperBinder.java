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

import static org.commonjava.rwx.binding.anno.AnnotationUtils.hasAnnotation;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

public class ArrayWrapperBinder
    extends AbstractBinder
    implements Binder
{

    private Object value;

    public ArrayWrapperBinder( final Binder parent, final Class<?> valueType, final BindingContext context )
    {
        super( parent, valueType, context );
    }

    @Override
    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( this.value == null )
        {
            this.value = value;
        }

        return this;
    }

    @Override
    protected Binder endArrayInternal()
        throws XmlRpcException
    {
        @SuppressWarnings( "unchecked" )
        final ValueType vt = hasAnnotation( getType(), ArrayPart.class ) ? ValueType.ARRAY : ValueType.STRUCT;
        setValue( value, vt );

        return this;
    }

    @Override
    protected Binder startArrayInternal()
        throws XmlRpcException
    {
        return this;
    }

    @Override
    protected Binder startArrayElementInternal( final int index )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    protected Binder valueInternal( final Object value, final ValueType type )
        throws XmlRpcException
    {
        return this;
    }

    @Override
    protected Binder endArrayElementInternal()
        throws XmlRpcException
    {
        return this;
    }
}
