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

package com.redhat.xmlrpc.model;

import com.redhat.xmlrpc.vocab.EventType;

public class RequestEvent
    implements Event
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

}
