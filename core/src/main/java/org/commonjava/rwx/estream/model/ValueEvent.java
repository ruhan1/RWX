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
package org.commonjava.rwx.estream.model;

import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

public class ValueEvent
    implements Event<Void>
{

    private final ValueType type;

    private final Object value;

    public ValueEvent( final Object value, final ValueType type )
    {
        this.value = value;
        this.type = type;
    }

    public ValueType getType()
    {
        return type;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public EventType getEventType()
    {
        return EventType.VALUE;
    }

    public boolean valueEventEquals( final Object value, final ValueType vType )
    {
        return eventEquals( EventType.VALUE, null, value, vType );
    }

    @Override
    public boolean eventEquals( final EventType eType, final Void key, final Object value, final ValueType vType )
    {
        if ( EventType.VALUE != eType )
        {
            return false;
        }
        else if ( type != null && type != vType )
        {
            return false;
        }
        else if ( this.value != null && !this.value.equals( value ) )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "ValueEvent [type=" + type + ", value=" + value + " (type: "
            + ( value == null ? "null" : value.getClass().getName() ) + ")]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final ValueEvent other = (ValueEvent) obj;
        if ( type == null )
        {
            if ( other.type != null )
            {
                return false;
            }
        }
        else if ( !type.equals( other.type ) )
        {
            return false;
        }
        if ( value == null )
        {
            if ( other.value != null )
            {
                return false;
            }
        }
        else if ( !value.equals( other.value ) )
        {
            return false;
        }
        return true;
    }
}
