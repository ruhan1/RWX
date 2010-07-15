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

package com.redhat.xmlrpc.impl.estream.model;

import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

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
        return "ValueEvent [type=" + type + ", value=" + value + "]";
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
