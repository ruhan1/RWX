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

public class RequestEvent
    implements Event<Void>
{

    private final EventType eventType;

    private final String method;

    public RequestEvent( final boolean start )
    {
        eventType = start ? EventType.START_REQUEST : EventType.END_REQUEST;
        method = null;
    }

    public RequestEvent( final String method )
    {
        this.method = method;
        eventType = EventType.REQUEST_METHOD;
    }

    public String getMethod()
    {
        return method;
    }

    @Override
    public EventType getEventType()
    {
        return eventType;
    }

    public boolean requestMethodEquals( final String method )
    {
        return eventEquals( EventType.REQUEST_METHOD, null, method, null );
    }

    @Override
    public boolean eventEquals( final EventType eType, final Void key, final Object value, final ValueType vType )
    {
        if ( eventType != eType )
        {
            return false;
        }
        else if ( method != null && !method.equals( value ) )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "RequestEvent [eventType=" + eventType + ", method=" + method + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( eventType == null ) ? 0 : eventType.hashCode() );
        result = prime * result + ( ( method == null ) ? 0 : method.hashCode() );
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
        final RequestEvent other = (RequestEvent) obj;
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
        if ( method == null )
        {
            if ( other.method != null )
            {
                return false;
            }
        }
        else if ( !method.equals( other.method ) )
        {
            return false;
        }
        return true;
    }
}
