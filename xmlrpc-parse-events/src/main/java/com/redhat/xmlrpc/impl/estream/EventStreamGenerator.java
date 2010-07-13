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

package com.redhat.xmlrpc.impl.estream;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.model.ArrayEvent;
import com.redhat.xmlrpc.model.Event;
import com.redhat.xmlrpc.model.ParameterEvent;
import com.redhat.xmlrpc.model.RequestEvent;
import com.redhat.xmlrpc.model.ResponseEvent;
import com.redhat.xmlrpc.model.StructEvent;
import com.redhat.xmlrpc.spi.XmlRpcGenerator;
import com.redhat.xmlrpc.spi.XmlRpcListener;

import java.util.LinkedList;
import java.util.List;

public class EventStreamGenerator
    implements XmlRpcGenerator
{

    private final LinkedList<Event> events = new LinkedList<Event>();

    private boolean locked = false;

    public EventStreamGenerator()
    {
    }

    public EventStreamGenerator( final List<Event> events )
    {
        locked = true;
        this.events.addAll( events );
    }

    public EventStreamGenerator add( final Event event )
    {
        if ( locked )
        {
            throw new IllegalStateException( "Cannot add events once generation has begun." );
        }

        events.addLast( event );
        return this;
    }

    @Override
    public void generate( final XmlRpcListener listener )
        throws XmlRpcException
    {
        locked = true;

        while ( !events.isEmpty() )
        {
            final Event e = events.removeFirst();
            switch ( e.getEventType() )
            {
                case START_ARRAY:
                {
                    listener.startArray();
                    break;
                }
                case ARRAY_ELEMENT:
                {
                    final ArrayEvent event = (ArrayEvent) e;
                    listener.arrayElement( event.getIndex(), event.getValue(), event.getValueType() );
                    break;
                }
                case END_ARRAY:
                {
                    listener.endArray();
                    break;
                }
                case START_STRUCT:
                {
                    listener.startStruct();
                    break;
                }
                case STRUCT_MEMBER:
                {
                    final StructEvent event = (StructEvent) e;
                    listener.structMember( event.getKey(), event.getValue(), event.getValueType() );
                    break;
                }
                case END_STRUCT:
                {
                    listener.endStruct();
                    break;
                }
                case START_COMPLEX_PARAMETER:
                {
                    final ParameterEvent event = (ParameterEvent) e;
                    listener.startComplexParameter( event.getIndex() );
                    break;
                }
                case END_COMPLEX_PARAMETER:
                {
                    listener.endComplexParameter();
                    break;
                }
                case PARAMETER:
                {
                    final ParameterEvent event = (ParameterEvent) e;
                    listener.parameter( event.getIndex(), event.getValue(), event.getValueType() );
                    break;
                }
                case START_REQUEST:
                {
                    listener.startRequest();
                    break;
                }
                case REQUEST_METHOD:
                {
                    final RequestEvent event = (RequestEvent) e;
                    listener.requestMethod( event.getMethod() );
                    break;
                }
                case END_REQUEST:
                {
                    listener.endRequest();
                    break;
                }
                case START_RESPONSE:
                {
                    listener.startResponse();
                    break;
                }
                case FAULT:
                {
                    final ResponseEvent event = (ResponseEvent) e;
                    listener.fault( event.getCode(), event.getMessage() );
                    break;
                }
                case END_RESPONSE:
                {
                    listener.endResponse();
                    break;
                }
                default:
                {
                    throw new XmlRpcException( "Invalid Event: " + e );
                }
            }
        }

        locked = false;
    }
}
