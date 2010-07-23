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

import org.apache.xbean.recipe.MapRecipe;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

public class MapBinder
    extends AbstractBinder
    implements Binder
{

    private MapRecipe recipe;

    private final Class<?> mapType;

    private String currentMember;

    private Object result;

    public MapBinder( final Binder parent, final Class<?> mapType, final Class<?> valueType,
                      final XBRBindingContext context )
    {
        super( parent, valueType, context );
        this.mapType = mapType;
    }

    @Override
    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        result = recipe.create();
        return this;
    }

    @Override
    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        recipe = new MapRecipe( mapType );
        return this;
    }

    @Override
    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        final Binder binder = getBindingContext().newBinder( this, getType() );
        if ( binder != null )
        {
            currentMember = key;
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
            getParent().value( result, ValueType.STRUCT );
            return getParent();
        }
        else if ( currentMember != null )
        {
            recipe.put( currentMember, value );
        }
        return this;
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        recipe.put( key, value );
        return this;
    }

    @Override
    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        currentMember = null;
        return this;
    }

}
