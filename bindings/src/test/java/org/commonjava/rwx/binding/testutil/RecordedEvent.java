/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.binding.testutil;

import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

public final class RecordedEvent
{

    private final EventType eventType;

    private final Object key;

    private final Object value;

    private final ValueType valueType;

    public RecordedEvent( final EventType eventType, final Object key, final Object value, final ValueType valueType )
    {
        this.eventType = eventType;
        this.key = key;
        this.value = value;
        this.valueType = valueType;
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public Object getKey()
    {
        return key;
    }

    public Object getValue()
    {
        return value;
    }

    public ValueType getValueType()
    {
        return valueType;
    }

    @Override
    public String toString()
    {
        return "RecordedEvent [eventType=" + eventType + ", key=" + key + ", value=" + value + "(type: "
            + ( value == null ? "null" : value.getClass().getName() ) + "), valueType=" + valueType + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( eventType == null ) ? 0 : eventType.hashCode() );
        result = prime * result + ( ( key == null ) ? 0 : key.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        result = prime * result + ( ( valueType == null ) ? 0 : valueType.hashCode() );
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
        final RecordedEvent other = (RecordedEvent) obj;
        if ( eventType == null )
        {
            if ( other.eventType != null )
            {
                return false;
            }
        }
        else if ( !eventType.equals( other.eventType ) )
        {
            return false;
        }
        if ( key == null )
        {
            if ( other.key != null )
            {
                return false;
            }
        }
        else if ( !key.equals( other.key ) )
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
        if ( valueType == null )
        {
            if ( other.valueType != null )
            {
                return false;
            }
        }
        else if ( !valueType.equals( other.valueType ) )
        {
            return false;
        }
        return true;
    }

}
