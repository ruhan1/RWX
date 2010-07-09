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

package com.redhat.xmlrpc.raw.model;

import com.redhat.xmlrpc.raw.type.ValueType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class XmlRpcArray
    extends ArrayList<XmlRpcValue>
    implements XmlRpcParameter<List<Object>>
{

    private static final long serialVersionUID = 1L;

    public static final ValueType VALUE_TYPE = ValueType.ARRAY;

    private boolean locked = false;

    public void lock()
    {
        locked = true;
    }

    public List<Object> rawValues()
    {
        final List<Object> result = new ArrayList<Object>();
        for ( final XmlRpcValue val : this )
        {
            result.add( val.getValue() );
        }

        return Collections.unmodifiableList( result );
    }

    @Override
    public void add( final int index, final XmlRpcValue element )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        super.add( index, element );
    }

    @Override
    public boolean add( final XmlRpcValue o )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        return super.add( o );
    }

    @Override
    public boolean addAll( final Collection<? extends XmlRpcValue> c )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        return super.addAll( c );
    }

    @Override
    public boolean addAll( final int index, final Collection<? extends XmlRpcValue> c )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        return super.addAll( index, c );
    }

    @Override
    public void clear()
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        super.clear();
    }

    @Override
    public XmlRpcValue remove( final int index )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        return super.remove( index );
    }

    @Override
    public boolean remove( final Object o )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        return super.remove( o );
    }

    @Override
    public XmlRpcValue set( final int index, final XmlRpcValue element )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( "List is locked." );
        }

        return super.set( index, element );
    }

    @Override
    public List<Object> getValue()
    {
        return rawValues();
    }

    @Override
    public ValueType getType()
    {
        return VALUE_TYPE;
    }

}
