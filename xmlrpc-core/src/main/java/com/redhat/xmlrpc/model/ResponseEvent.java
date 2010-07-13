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

public class ResponseEvent
    implements Event
{

    private final EventType eventType;

    private final int code;

    private final String message;

    public ResponseEvent( final boolean start )
    {
        eventType = start ? EventType.START_RESPONSE : EventType.END_RESPONSE;
        code = -1;
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
        return code;
    }

    public String getMessage()
    {
        return message;
    }

}
