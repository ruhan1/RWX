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

import org.apache.xbean.recipe.CollectionRecipe;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.List;

public class CollectionBinder
    extends AbstractBinder
    implements Binder
{

    private List<Object> values;

    private final Class<?> collectionType;

    private int currentIndex;

    private Object result;

    public CollectionBinder( final Binder parent, final Class<?> collectionType, final Class<?> valueType,
                             final XBRBindingContext context )
    {
        super( parent, valueType, context );
        this.collectionType = collectionType;
    }

    @Override
    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentIndex < 0 )
        {
            addValue( index, value );
        }

        return this;
    }

    private void addValue( final int index, final Object value )
    {
        while ( values.size() <= index )
        {
            values.add( null );
        }

        values.set( index, value );
    }

    protected List<Object> getValues()
    {
        return values;
    }

    @Override
    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        result = create();

        return this;
    }

    protected Object create()
    {
        final CollectionRecipe recipe = new CollectionRecipe( collectionType );
        recipe.addAll( getValues() );
        return recipe.create();
    }

    @Override
    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        values = new ArrayList<Object>();
        return this;
    }

    @Override
    public XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        final Binder binder = getBindingContext().newBinder( this, getType() );
        if ( binder != null )
        {
            currentIndex = index;
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
            getParent().value( result, ValueType.ARRAY );
            return getParent();
        }
        else if ( currentIndex > -1 )
        {
            addValue( currentIndex, value );
        }
        return this;
    }

    @Override
    public XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        currentIndex = -1;
        return this;
    }

}
