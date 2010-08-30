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

package org.commonjava.rwx.estream.model;

import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

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
