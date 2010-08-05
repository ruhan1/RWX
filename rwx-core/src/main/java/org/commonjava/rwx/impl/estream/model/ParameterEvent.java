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

package org.commonjava.rwx.impl.estream.model;

import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

public class ParameterEvent
    implements Event<Integer>
{

    private final EventType eventType;

    private final int index;

    private final Object value;

    private final ValueType valueType;

    public ParameterEvent()
    {
        eventType = EventType.END_PARAMETER;

        index = -1;
        value = null;
        valueType = null;
    }

    public ParameterEvent( final int index )
    {
        eventType = EventType.START_PARAMETER;

        this.index = index;
        value = null;
        valueType = null;
    }

    public ParameterEvent( final int index, final Object value, final ValueType valueType )
    {
        this.index = index;
        this.value = value;
        this.valueType = valueType;

        eventType = EventType.PARAMETER;
    }

    @Override
    public EventType getEventType()
    {
        return eventType;
    }

    public int getIndex()
    {
        return index;
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
    public boolean eventEquals( final EventType eType, final Integer key, final Object value, final ValueType vType )
    {
        if ( eventType != eType )
        {
            return false;
        }
        else if ( index > -1 && !Integer.valueOf( index ).equals( key ) )
        {
            return false;
        }
        else if ( valueType != null && valueType != vType )
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
        return "ParameterEvent [eventType=" + eventType + ", index=" + index + ", value=" + value + ", valueType="
            + valueType + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( eventType == null ) ? 0 : eventType.hashCode() );
        result = prime * result + index;
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
        final ParameterEvent other = (ParameterEvent) obj;
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
        if ( index != other.index )
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
