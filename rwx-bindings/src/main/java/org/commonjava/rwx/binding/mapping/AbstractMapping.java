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

package org.commonjava.rwx.binding.mapping;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMapping<T>
    implements Mapping<T>
{

    private static final long serialVersionUID = 1L;

    private Class<?> objectType;

    private T[] constructorKeys;

    private Map<T, FieldBinding> bindings = new HashMap<T, FieldBinding>();

    protected AbstractMapping( final Class<?> objectType, final T... constructorKeys )
    {
        this.objectType = objectType;
        this.constructorKeys = constructorKeys;
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

    public final Class<?> getObjectType()
    {
        return objectType;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void readExternal( final ObjectInput in )
        throws IOException, ClassNotFoundException
    {
        objectType = Class.forName( (String) in.readObject() );
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
        out.writeObject( objectType.getName() );
        out.writeObject( constructorKeys );

        out.writeInt( bindings.size() );
        for ( final Map.Entry<T, FieldBinding> binding : bindings.entrySet() )
        {
            out.writeObject( binding.getKey() );
            out.writeObject( binding.getValue() );
        }
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [objectType=" + objectType + "]";
    }

}
