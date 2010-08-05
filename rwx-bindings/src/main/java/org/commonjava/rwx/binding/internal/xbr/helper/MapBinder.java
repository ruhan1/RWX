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

    private final MapRecipe recipe;

    private String currentMember;

    public MapBinder( final Binder parent, final Class<?> mapType, final Class<?> valueType,
                      final XBRBindingContext context )
    {
        super( parent, valueType, context );
        recipe = new MapRecipe( mapType );
    }

    @Override
    protected Binder endStructInternal()
        throws XmlRpcException
    {
        setValue( recipe.create(), ValueType.STRUCT );
        return this;
    }

    @Override
    protected Binder startStructMemberInternal( final String key )
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
    protected Binder valueInternal( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentMember != null )
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
    protected Binder endStructMemberInternal()
        throws XmlRpcException
    {
        currentMember = null;
        return this;
    }

}
