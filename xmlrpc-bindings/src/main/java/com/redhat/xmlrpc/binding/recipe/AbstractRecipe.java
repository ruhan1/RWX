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

package com.redhat.xmlrpc.binding.recipe;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRecipe<T>
    implements Recipe<T>
{

    private static final long serialVersionUID = 1L;

    private String name;

    private String objectType;

    private T[] constructorKeys;

    private Map<T, FieldBinding> bindings = new HashMap<T, FieldBinding>();

    protected AbstractRecipe( final String name, final String objectType, final T... constructorKeys )
    {
        this.objectType = objectType;
        this.constructorKeys = constructorKeys;
        this.name = name.startsWith( "recipe:" ) ? name : "recipe:" + name;
    }

    protected void putBinding( final T key, final FieldBinding binding )
    {
        bindings.put( key, binding );
    }

    @Override
    public final T[] getConstructorKeys()
    {
        return constructorKeys;
    }

    @Override
    public final Map<T, FieldBinding> getFieldBindings()
    {
        return bindings;
    }

    public final FieldBinding getFieldBinding( final T key )
    {
        return bindings.get( key );
    }

    public final String getName()
    {
        return name;
    }

    public final String getObjectType()
    {
        return objectType;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void readExternal( final ObjectInput in )
        throws IOException, ClassNotFoundException
    {
        name = (String) in.readObject();
        objectType = (String) in.readObject();
        constructorKeys = (T[]) in.readObject();

        final int bindingCount = in.readInt();
        bindings = new HashMap<T, FieldBinding>();
        for ( int i = 0; i < bindingCount; i++ )
        {
            final T key = (T) in.readObject();
            final FieldBinding binding = (FieldBinding) in.readObject();
            bindings.put( key, binding );
        }
    }

    @Override
    public void writeExternal( final ObjectOutput out )
        throws IOException
    {
        out.writeObject( name );
        out.writeObject( objectType );
        out.writeObject( constructorKeys );

        out.writeInt( bindings.size() );
        for ( final Map.Entry<T, FieldBinding> binding : bindings.entrySet() )
        {
            out.writeObject( binding.getKey() );
            out.writeObject( binding.getValue() );
        }
    }

}
