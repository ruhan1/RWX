/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.binding.internal.xbr.helper;

import org.apache.xbean.recipe.ObjectRecipe;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.StructMapping;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.lang.reflect.Field;

public class StructMappingBinder
    extends AbstractMappingBinder<StructMapping>
    implements Binder
{

    private final ObjectRecipe recipe;

    private FieldBinding currentField;

    private boolean ignore = false;

    private int level = 0;

    public StructMappingBinder( final Binder parent, final Class<?> type, final StructMapping mapping,
                                final XBRBindingContext context )
    {
        super( parent, type, mapping, context );
        recipe = XBRBindingContext.setupObjectRecipe( mapping );
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( !ignore && currentField == null && value != null )
        {
            final FieldBinding binding = getMapping().getFieldBinding( key );

            // if ignore == false and the current field is null, the binding MUST be non-null.  
            recipe.setProperty( binding.getFieldName(),
                                type.coercion().fromString( value == null ? null : String.valueOf( value ) ) );
        }

        return this;
    }

    @Override
    protected Binder startStructMemberInternal( final String key )
        throws XmlRpcException
    {
        if ( ignore )
        {
            level++;
        }
        else
        {
            final FieldBinding binding = getMapping().getFieldBinding( key );
            if ( binding == null )
            {
                ignore = true;
                level = 0;
                return this;
            }

            final Field field = getBindingContext().findField( binding, getType() );

            final Binder binder = getBindingContext().newBinder( this, field );
            if ( binder != null )
            {
                currentField = binding;
                return binder;
            }
        }

        return this;
    }

    @Override
    protected Binder valueInternal( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( !ignore )
        {
            if ( currentField != null )
            {
                recipe.setProperty( currentField.getFieldName(), value );
            }
        }

        return this;
    }

    @Override
    protected Binder startStructInternal()
        throws XmlRpcException
    {
        if ( ignore )
        {
            level++;
        }

        return this;
    }

    @Override
    protected Binder endStructInternal()
        throws XmlRpcException
    {
        if ( ignore )
        {
            level--;
        }
        else
        {
            setValue( recipe.create(), ValueType.STRUCT );
        }

        return this;
    }

    @Override
    protected Binder endStructMemberInternal()
        throws XmlRpcException
    {
        if ( ignore )
        {
            if ( level == 0 )
            {
                currentField = null;
                ignore = false;
            }
            else
            {
                level--;
            }
        }
        else
        {
            currentField = null;
        }

        return this;
    }

}
