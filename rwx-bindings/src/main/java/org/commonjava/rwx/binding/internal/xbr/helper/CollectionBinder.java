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
    protected Binder endArrayInternal()
        throws XmlRpcException
    {
        setValue( create(), ValueType.ARRAY );

        return this;
    }

    protected Object create()
    {
        final CollectionRecipe recipe = new CollectionRecipe( collectionType );
        recipe.addAll( getValues() );
        return recipe.create();
    }

    @Override
    protected Binder startArrayInternal()
        throws XmlRpcException
    {
        values = new ArrayList<Object>();
        return this;
    }

    @Override
    protected Binder startArrayElementInternal( final int index )
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
    protected Binder valueInternal( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentIndex > -1 )
        {
            addValue( currentIndex, value );
        }

        return this;
    }

    @Override
    protected Binder endArrayElementInternal()
        throws XmlRpcException
    {
        currentIndex = -1;
        return this;
    }

}
