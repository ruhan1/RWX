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

package org.commonjava.rwx.impl.estream;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamParser;
import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.List;

public class EventStreamParserImpl
    implements EventStreamParser
{

    private final List<Event<?>> events = new ArrayList<Event<?>>();

    @Override
    public List<Event<?>> getEvents()
    {
        return events;
    }

    @Override
    public EventStreamParserImpl arrayElement( final int index, final Object value, final ValueType type )
    {
        events.add( new ArrayEvent( index, value, type ) );
        return this;
    }

    @Override
    public EventStreamParserImpl endArray()
    {
        events.add( new ArrayEvent( EventType.END_ARRAY ) );
        return this;
    }

    @Override
    public EventStreamParserImpl endParameter()
    {
        events.add( new ParameterEvent() );
        return this;
    }

    @Override
    public EventStreamParserImpl endRequest()
    {
        events.add( new RequestEvent( false ) );
        return this;
    }

    @Override
    public EventStreamParserImpl endResponse()
    {
        events.add( new ResponseEvent( false ) );
        return this;
    }

    @Override
    public EventStreamParserImpl endStruct()
    {
        events.add( new StructEvent( EventType.END_STRUCT ) );
        return this;
    }

    @Override
    public EventStreamParserImpl fault( final int code, final String message )
    {
        events.add( new ResponseEvent( code, message ) );
        return this;
    }

    @Override
    public EventStreamParserImpl parameter( final int index, final Object value, final ValueType type )
    {
        events.add( new ParameterEvent( index, value, type ) );
        return this;
    }

    @Override
    public EventStreamParserImpl requestMethod( final String methodName )
    {
        events.add( new RequestEvent( methodName ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startArray()
    {
        events.add( new ArrayEvent( EventType.START_ARRAY ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startParameter( final int index )
    {
        events.add( new ParameterEvent( index ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startRequest()
    {
        events.add( new RequestEvent( true ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startResponse()
    {
        events.add( new ResponseEvent( true ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startStruct()
    {
        events.add( new StructEvent( EventType.START_STRUCT ) );
        return this;
    }

    @Override
    public EventStreamParserImpl structMember( final String key, final Object value, final ValueType type )
    {
        events.add( new StructEvent( key, value, type ) );
        return this;
    }

    @Override
    public EventStreamParserImpl endArrayElement()
        throws XmlRpcException
    {
        events.add( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) );
        return this;
    }

    @Override
    public EventStreamParserImpl endStructMember()
        throws XmlRpcException
    {
        events.add( new StructEvent( EventType.END_STRUCT_MEMBER ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startArrayElement( final int index )
        throws XmlRpcException
    {
        events.add( new ArrayEvent( index ) );
        return this;
    }

    @Override
    public EventStreamParserImpl startStructMember( final String key )
        throws XmlRpcException
    {
        events.add( new StructEvent( key ) );
        return this;
    }

    @Override
    public EventStreamParserImpl value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        events.add( new ValueEvent( value, type ) );
        return this;
    }

}
