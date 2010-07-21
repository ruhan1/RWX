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

package org.commonjava.rwx.impl.estream.model;

import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

public class ResponseEvent
    implements Event<Integer>
{

    private final EventType eventType;

    private final Integer code;

    private final String message;

    public ResponseEvent( final boolean start )
    {
        eventType = start ? EventType.START_RESPONSE : EventType.END_RESPONSE;
        code = null;
        message = null;
    }

    public ResponseEvent( final int code, final String message )
    {
        this.code = code;
        this.message = message;
        eventType = EventType.FAULT;
    }

    @Override
    public EventType getEventType()
    {
        return eventType;
    }

    public int getCode()
    {
        return code == null ? Integer.MIN_VALUE : code;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean responseFaultEquals( final int code, final String message )
    {
        return eventEquals( EventType.FAULT, code, message, ValueType.STRING );
    }

    @Override
    public boolean eventEquals( final EventType eType, final Integer code, final Object value, final ValueType vType )
    {
        if ( eventType != eType )
        {
            return false;
        }
        else if ( code != null && !this.code.equals( code ) )
        {
            return false;
        }
        else if ( message != null && !message.equals( value ) )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "ResponseEvent [code=" + code + ", eventType=" + eventType + ", message=" + message + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( code == null ) ? 0 : code.hashCode() );
        result = prime * result + ( ( eventType == null ) ? 0 : eventType.hashCode() );
        result = prime * result + ( ( message == null ) ? 0 : message.hashCode() );
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
        final ResponseEvent other = (ResponseEvent) obj;
        if ( code == null )
        {
            if ( other.code != null )
            {
                return false;
            }
        }
        else if ( !code.equals( other.code ) )
        {
            return false;
        }
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
        if ( message == null )
        {
            if ( other.message != null )
            {
                return false;
            }
        }
        else if ( !message.equals( other.message ) )
        {
            return false;
        }
        return true;
    }
}
