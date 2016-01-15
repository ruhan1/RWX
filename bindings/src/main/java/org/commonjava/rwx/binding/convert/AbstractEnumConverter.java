/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
