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

import org.apache.xbean.recipe.ObjectRecipe;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.lang.reflect.Field;

public class ArrayMappingBinder
    extends AbstractMappingBinder<ArrayMapping>
    implements Binder
{

    private final ObjectRecipe recipe;

    private FieldBinding currentField;

    private boolean ignore = false;

    private int level = 0;

    public ArrayMappingBinder( final Binder parent, final Class<?> type, final ArrayMapping mapping,
                               final XBRBindingContext context )
    {
        super( parent, type, mapping, context );
        recipe = XBRBindingContext.setupObjectRecipe( mapping );
    }

    @Override
    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( !ignore && currentField == null && value != null )
        {
            final FieldBinding binding = getMapping().getFieldBinding( index );

            // if ignore == false and the current field is null, the binding MUST be non-null.  
            recipe.setProperty( binding.getFieldName(),
                                type.coercion().fromString( value == null ? null : String.valueOf( value ) ) );
        }

        return this;
    }

    @Override
    protected Binder startArrayElementInternal( final int index )
        throws XmlRpcException
    {
        if ( ignore )
        {
            level++;
        }
        else
        {
            final FieldBinding binding = getMapping().getFieldBinding( index );
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
    protected Binder startArrayInternal()
    {
        if ( ignore )
        {
            level++;
        }

        return this;
    }

    @Override
    protected Binder endArrayInternal()
        throws XmlRpcException
    {
        if ( ignore )
        {
            level--;
        }
        else
        {
            setValue( recipe.create(), ValueType.ARRAY );
        }
        return this;
    }

    @Override
    protected Binder endArrayElementInternal()
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
