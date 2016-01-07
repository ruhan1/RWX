/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
