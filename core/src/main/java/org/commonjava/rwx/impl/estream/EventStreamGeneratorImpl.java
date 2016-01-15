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
package org.commonjava.rwx.impl.estream;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamGenerator;
import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class EventStreamGeneratorImpl
    implements EventStreamGenerator
{

    private final LinkedList<Event<?>> events = new LinkedList<Event<?>>();

    private boolean locked = false;

    public EventStreamGeneratorImpl()
    {
    }

    public EventStreamGeneratorImpl( final List<Event<?>> events )
    {
        locked = true;
        this.events.addAll( events );
    }

    @Override
    public List<Event<?>> getEvents()
    {
        return events;
    }

    @Override
    public EventStreamGenerator clear()
    {
        checkLocked();
        events.clear();

        return this;
    }

    @Override
    public EventStreamGeneratorImpl add( final Event<?> event )
    {
        checkLocked();
        events.addLast( event );
        return this;
    }

    private void checkLocked()
    {
        if ( locked )
        {
            throw new IllegalStateException( "Cannot add events once generation has begun." );
        }
    }

    @Override
    public EventStreamGeneratorImpl generate( final XmlRpcListener l )
        throws XmlRpcException
    {
        final XmlRpcListener listener = new TrackingXmlRpcListener( l );
        locked = true;

        try
        {
            for ( final Event<?> e : events )
            {
                Logger logger = LoggerFactory.getLogger( getClass() );
                logger.trace( "Firing: {} into listener: {}", e, listener );

                switch ( e.getEventType() )
                {
                    case VALUE:
                    {
                        final ValueEvent event = (ValueEvent) e;
                        listener.value( event.getValue(), event.getType() );
                        break;
                    }
                    case START_ARRAY:
                    {
                        listener.startArray();
                        break;
                    }
                    case START_ARRAY_ELEMENT:
                    {
                        final ArrayEvent event = (ArrayEvent) e;
                        listener.startArrayElement( event.getIndex() );
                        break;
                    }
                    case END_ARRAY_ELEMENT:
                    {
                        listener.endArrayElement();
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
                    case START_STRUCT_MEMBER:
                    {
                        final StructEvent event = (StructEvent) e;
                        listener.startStructMember( event.getKey() );
                        break;
                    }
                    case END_STRUCT_MEMBER:
                    {
                        listener.endStructMember();
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
                    case START_PARAMETER:
                    {
                        final ParameterEvent event = (ParameterEvent) e;
                        listener.startParameter( event.getIndex() );
                        break;
                    }
                    case END_PARAMETER:
                    {
                        listener.endParameter();
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
        }
        finally
        {
            locked = false;
        }

        return this;
    }
}
