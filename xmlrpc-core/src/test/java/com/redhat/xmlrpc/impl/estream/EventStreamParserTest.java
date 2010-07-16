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

import static com.redhat.xmlrpc.impl.estream.testutil.EventAssertions.assertEvents;

import org.junit.Test;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.estream.model.ArrayEvent;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.ParameterEvent;
import com.redhat.xmlrpc.impl.estream.model.RequestEvent;
import com.redhat.xmlrpc.impl.estream.model.ResponseEvent;
import com.redhat.xmlrpc.impl.estream.model.StructEvent;
import com.redhat.xmlrpc.impl.estream.model.ValueEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

import java.util.List;

public class EventStreamParserTest
{

    @Test
    public void simpleRequest()
    {
        final List<Event<?>> check =
            new ExtList<Event<?>>().with( new RequestEvent( true ) )
                                   .with( new RequestEvent( "foo" ) )
                                   .with( new RequestEvent( false ) );

        final EventStreamParser parser = new EventStreamParser();
        parser.startRequest().requestMethod( "foo" ).endRequest();

        assertEvents( check, parser.getEvents() );
    }

    @Test
    public void requestWithOneParam()
        throws XmlRpcException
    {
        final List<Event<?>> check =
            new ExtList<Event<?>>().with( new RequestEvent( true ) )
                                   .with( new RequestEvent( "foo" ) )
                                   .with( new ParameterEvent( 0 ) )
                                   .with( new ValueEvent( "foo", ValueType.STRING ) )
                                   .with( new ParameterEvent( 0, "foo", ValueType.STRING ) )
                                   .with( new ParameterEvent() )
                                   .with( new RequestEvent( false ) );

        final EventStreamParser parser = new EventStreamParser();
        parser.startRequest()
              .requestMethod( "foo" )
              .startParameter( 0 )
              .value( "foo", ValueType.STRING )
              .parameter( 0, "foo", ValueType.STRING )
              .endParameter()
              .endRequest();

        assertEvents( check, parser.getEvents() );
    }

    @Test
    public void simpleResponse()
    {
        final List<Event<?>> check =
            new ExtList<Event<?>>().with( new ResponseEvent( true ) )
                                   .with( new ResponseEvent( 101, "foo" ) )
                                   .with( new ResponseEvent( false ) );

        final EventStreamParser parser = new EventStreamParser();
        parser.startResponse().fault( 101, "foo" ).endResponse();

        assertEvents( check, parser.getEvents() );
    }

    @Test
    public void simpleOneElementArray()
        throws XmlRpcException
    {
        final List<Event<?>> check =
            new ExtList<Event<?>>().with( new ArrayEvent( EventType.START_ARRAY ) )
                                   .with( new ArrayEvent( 0 ) )
                                   .with( new ValueEvent( "foo", ValueType.STRING ) )
                                   .with( new ArrayEvent( 0, "foo", ValueType.STRING ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY ) );

        final EventStreamParser parser = new EventStreamParser();
        parser.startArray()
              .startArrayElement( 0 )
              .value( "foo", ValueType.STRING )
              .arrayElement( 0, "foo", ValueType.STRING )
              .endArrayElement()
              .endArray();

        assertEvents( check, parser.getEvents() );
    }

    @Test
    public void simpleOneMemberStruct()
        throws XmlRpcException
    {
        final List<Event<?>> check =
            new ExtList<Event<?>>().with( new StructEvent( EventType.START_STRUCT ) )
                                   .with( new StructEvent( "key" ) )
                                   .with( new ValueEvent( "foo", ValueType.STRING ) )
                                   .with( new StructEvent( "key", "foo", ValueType.STRING ) )
                                   .with( new StructEvent( EventType.END_STRUCT_MEMBER ) )
                                   .with( new StructEvent( EventType.END_STRUCT ) );

        final EventStreamParser parser = new EventStreamParser();
        parser.startStruct()
              .startStructMember( "key" )
              .value( "foo", ValueType.STRING )
              .structMember( "key", "foo", ValueType.STRING )
              .endStructMember()
              .endStruct();

        assertEvents( check, parser.getEvents() );
    }

    @Test
    public void simpleStructWithEmbeddedArray()
        throws XmlRpcException
    {
        final List<Event<?>> check =
            new ExtList<Event<?>>().with( new StructEvent( EventType.START_STRUCT ) )
                                   .with( new StructEvent( "key" ) )
                                   .with( new ArrayEvent( EventType.START_ARRAY ) )
                                   .with( new ArrayEvent( 0 ) )
                                   .with( new ValueEvent( "foo", ValueType.STRING ) )
                                   .with( new ArrayEvent( 0, "foo", ValueType.STRING ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY ) )
                                   .with( new StructEvent( "key", new ExtList<String>( "foo" ), ValueType.ARRAY ) )
                                   .with( new StructEvent( EventType.END_STRUCT_MEMBER ) )
                                   .with( new StructEvent( EventType.END_STRUCT ) );

        final EventStreamParser parser = new EventStreamParser();
        parser.startStruct()
              .startStructMember( "key" )
              .startArray()
              .startArrayElement( 0 )
              .value( "foo", ValueType.STRING )
              .arrayElement( 0, "foo", ValueType.STRING )
              .endArrayElement()
              .endArray()
              .structMember( "key", new ExtList<String>( "foo" ), ValueType.ARRAY )
              .endStructMember()
              .endStruct();

        assertEvents( check, parser.getEvents() );
    }

}
