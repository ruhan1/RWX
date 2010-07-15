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
import com.redhat.xmlrpc.impl.estream.model.ArrayEvent;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.ParameterEvent;
import com.redhat.xmlrpc.impl.estream.model.RequestEvent;
import com.redhat.xmlrpc.impl.estream.model.ResponseEvent;
import com.redhat.xmlrpc.impl.estream.model.StructEvent;
import com.redhat.xmlrpc.impl.estream.model.ValueEvent;
import com.redhat.xmlrpc.spi.XmlRpcListener;
import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

import java.util.ArrayList;
import java.util.List;

public class EventStreamParser
    implements XmlRpcListener
{

    private final List<Event<?>> events = new ArrayList<Event<?>>();

    public List<Event<?>> getEvents()
    {
        return events;
    }

    @Override
    public EventStreamParser arrayElement( final int index, final Object value, final ValueType type )
    {
        events.add( new ArrayEvent( index, value, type ) );
        return this;
    }

    @Override
    public EventStreamParser endArray()
    {
        events.add( new ArrayEvent( EventType.END_ARRAY ) );
        return this;
    }

    @Override
    public EventStreamParser endParameter()
    {
        events.add( new ParameterEvent() );
        return this;
    }

    @Override
    public EventStreamParser endRequest()
    {
        events.add( new RequestEvent( false ) );
        return this;
    }

    @Override
    public EventStreamParser endResponse()
    {
        events.add( new ResponseEvent( false ) );
        return this;
    }

    @Override
    public EventStreamParser endStruct()
    {
        events.add( new StructEvent( EventType.END_STRUCT ) );
        return this;
    }

    @Override
    public EventStreamParser fault( final int code, final String message )
    {
        events.add( new ResponseEvent( code, message ) );
        return this;
    }

    @Override
    public EventStreamParser parameter( final int index, final Object value, final ValueType type )
    {
        events.add( new ParameterEvent( index, value, type ) );
        return this;
    }

    @Override
    public EventStreamParser requestMethod( final String methodName )
    {
        events.add( new RequestEvent( methodName ) );
        return this;
    }

    @Override
    public EventStreamParser startArray()
    {
        events.add( new ArrayEvent( EventType.START_ARRAY ) );
        return this;
    }

    @Override
    public EventStreamParser startParameter( final int index )
    {
        events.add( new ParameterEvent( index ) );
        return this;
    }

    @Override
    public EventStreamParser startRequest()
    {
        events.add( new RequestEvent( true ) );
        return this;
    }

    @Override
    public EventStreamParser startResponse()
    {
        events.add( new ResponseEvent( true ) );
        return this;
    }

    @Override
    public EventStreamParser startStruct()
    {
        events.add( new StructEvent( EventType.START_STRUCT ) );
        return this;
    }

    @Override
    public EventStreamParser structMember( final String key, final Object value, final ValueType type )
    {
        events.add( new StructEvent( key, value, type ) );
        return this;
    }

    @Override
    public EventStreamParser endArrayElement()
        throws XmlRpcException
    {
        events.add( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) );
        return this;
    }

    @Override
    public EventStreamParser endStructMember()
        throws XmlRpcException
    {
        events.add( new StructEvent( EventType.END_STRUCT_MEMBER ) );
        return this;
    }

    @Override
    public EventStreamParser startArrayElement( final int index )
        throws XmlRpcException
    {
        events.add( new ArrayEvent( index ) );
        return this;
    }

    @Override
    public EventStreamParser startStructMember( final String key )
        throws XmlRpcException
    {
        events.add( new StructEvent( key ) );
        return this;
    }

    @Override
    public EventStreamParser value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        events.add( new ValueEvent( value, type ) );
        return this;
    }

}
