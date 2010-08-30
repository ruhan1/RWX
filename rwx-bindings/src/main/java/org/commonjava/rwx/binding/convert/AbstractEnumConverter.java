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

import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.binding.spi.value.AbstractSimpleValueBinder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.util.Map;

public abstract class AbstractEnumConverter
    extends AbstractSimpleValueBinder
{

    protected AbstractEnumConverter( final Binder parent, final Class<?> type, final BindingContext context )
    {
        super( parent, type, context );
    }

    @Override
    public final void generate( final XmlRpcListener listener, final Object value,
                                final Map<Class<?>, Mapping<?>> recipes )
        throws XmlRpcException
    {
        listener.value( generateEnumValue( value ), value == null ? ValueType.NIL : getGeneratedValueType( value ) );
    }

    @Override
    public final XmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( ( value instanceof Integer ) )
        {
            final int idx = ( (Integer) value ).intValue();
            getParent().value( getEnumValue( idx ), ValueType.STRUCT );
        }
        else if ( value != null )
        {
            final String name = String.valueOf( value );
            getParent().value( getEnumValue( name ), ValueType.STRUCT );
        }
        else
        {
            getParent().value( getDefaultEnumValue(), ValueType.STRUCT );
        }

        return getParent();
    }

    protected abstract Object getEnumValue( int idx );

    protected abstract Object getEnumValue( String name );

    protected abstract Object getDefaultEnumValue();

    protected abstract Object generateEnumValue( Object value );

    protected abstract ValueType getGeneratedValueType( Object value );

}
