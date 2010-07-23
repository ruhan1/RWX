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
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.lang.reflect.Field;

public class MessageBinder
    extends AbstractMappingBinder<ArrayMapping>
    implements Binder
{

    private ObjectRecipe recipe;

    private FieldBinding currentField;

    public MessageBinder( final Class<?> type, final ArrayMapping mapping, final XBRBindingContext context )
    {
        super( null, type, mapping, context );
    }

    public Object create()
    {
        return recipe.create();
    }

    @Override
    public XmlRpcListener parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentField == null )
        {
            final FieldBinding binding = getMapping().getFieldBinding( index );
            recipe.setProperty( binding.getFieldName(), value );
        }

        return this;
    }

    @Override
    public XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        final FieldBinding binding = getMapping().getFieldBinding( index );
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
        if ( currentField != null )
        {
            recipe.setProperty( currentField.getFieldName(), value );
        }

        return this;
    }

    @Override
    public XmlRpcListener fault( final int code, final String message )
        throws XmlRpcException
    {
        throw new XmlRpcFaultException( code, message );
    }

    @Override
    public XmlRpcListener startRequest()
        throws XmlRpcException
    {
        recipe = XBRBindingContext.setupObjectRecipe( getMapping() );
        return this;
    }

    @Override
    public XmlRpcListener startResponse()
        throws XmlRpcException
    {
        recipe = XBRBindingContext.setupObjectRecipe( getMapping() );
        return this;
    }

    @Override
    public XmlRpcListener endParameter()
        throws XmlRpcException
    {
        currentField = null;
        return this;
    }

}
