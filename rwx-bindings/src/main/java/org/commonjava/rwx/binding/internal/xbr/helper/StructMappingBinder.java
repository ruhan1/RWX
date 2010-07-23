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

    private ObjectRecipe recipe;

    private FieldBinding currentField;

    private Object result;

    private boolean ignore = false;

    public StructMappingBinder( final Binder parent, final Class<?> type, final StructMapping mapping,
                                final XBRBindingContext context )
    {
        super( parent, type, mapping, context );
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( !ignore && currentField == null )
        {
            final FieldBinding binding = getMapping().getFieldBinding( key );
            recipe.setProperty( binding.getFieldName(), type.coercion().fromString( (String) value ) );
        }

        return this;
    }

    @Override
    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        final FieldBinding binding = getMapping().getFieldBinding( key );
        if ( binding == null )
        {
            ignore = true;
            return this;
        }

        final Field field = getBindingContext().findField( binding, getType() );

        final Binder binder = getBindingContext().newBinder( this, field );
        if ( binder != null )
        {
            currentField = binding;
            return binder;
        }

        return this;
    }

    @Override
    public XmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( result != null )
        {
            final Binder parent = getParent();
            parent.value( result, ValueType.STRUCT );
            return parent;
        }
        else if ( currentField != null )
        {
            recipe.setProperty( currentField.getFieldName(), value );
        }

        return this;
    }

    @Override
    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        recipe = XBRBindingContext.setupObjectRecipe( getMapping() );
        return this;
    }

    @Override
    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        result = recipe.create();

        return this;
    }

    @Override
    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        currentField = null;
        ignore = false;
        return this;
    }

}
