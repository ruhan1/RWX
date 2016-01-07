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

import org.commonjava.rwx.binding.spi.value.ValueBinder;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class FieldBinding
    implements Externalizable, Serializable
{

    private static final long serialVersionUID = 1L;

    private String name;

    private Class<?> type;

    private Class<? extends ValueBinder> valueBinder;

    public FieldBinding( final String name, final Class<?> type )
    {
        this.name = name;
        this.type = type;
    }

    public FieldBinding withValueBinderType( final Class<? extends ValueBinder> valueBinder )
    {
        this.valueBinder = valueBinder;
        return this;
    }

    public Class<? extends ValueBinder> getValueBinderType()
    {
        return valueBinder;
    }

    public String getFieldName()
    {
        return name;
    }

    public Class<?> getFieldType()
    {
        return type;
    }

    @Override
    public void readExternal( final ObjectInput in )
        throws IOException, ClassNotFoundException
    {
        name = (String) in.readObject();
        type = Class.forName( (String) in.readObject() );
    }

    @Override
    public void writeExternal( final ObjectOutput out )
        throws IOException
    {
        out.writeObject( name );
        out.writeObject( type.getName() );
    }

    @Override
    public String toString()
    {
        return "FieldBinding [name=" + name + ", type=" + type + ", valueBinder=" + valueBinder + "]";
    }

}
