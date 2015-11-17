/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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

import static org.commonjava.rwx.impl.estream.testutil.EventAssertions.assertRecordedEvents;
import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamGenerator;
import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.impl.estream.EventStreamGeneratorImpl;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.ExtMap;
import org.commonjava.rwx.impl.estream.testutil.RecordingListener;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


public class EventStreamGeneratorTest
{

    @Test
    public void addThenClearLeadsToZeroEvents()
    {
        final EventStreamGenerator gennie = new EventStreamGeneratorImpl();

        assertEquals( 0, gennie.getEvents().size() );

        gennie.add( new ResponseEvent( false ) );

        assertEquals( 1, gennie.getEvents().size() );

        gennie.clear();

        assertEquals( 0, gennie.getEvents().size() );
    }

    @Test
    public void simpleRequest()
        throws XmlRpcException
    {
        final ExtList<Event<?>> events =
            new ExtList<Event<?>>().with( new RequestEvent( true ) )
                                   .with( new RequestEvent( "foo" ) )
                                   .with( new RequestEvent( false ) );

        final RecordingListener listener = new RecordingListener();
        new EventStreamGeneratorImpl( events ).generate( listener );

        assertRecordedEvents( events, listener.getRecordedEvents() );
    }

    @Test
    public void simpleIncremental()
        throws XmlRpcException
    {
        final RecordingListener listener = new RecordingListener();
        final EventStreamGeneratorImpl gennie =
            new EventStreamGeneratorImpl().add( new RequestEvent( true ) )
                                      .add( new RequestEvent( "foo" ) )
                                      .add( new RequestEvent( false ) );

        gennie.generate( listener );

        assertRecordedEvents( gennie.getEvents(), listener.getRecordedEvents() );
    }

    @Test
    public void responseWithFault()
        throws XmlRpcException
    {
        final ExtList<Event<?>> events =
            new ExtList<Event<?>>().with( new ResponseEvent( true ) )
                                   .with( new ResponseEvent( 1, "Test fault" ) )
                                   .with( new ResponseEvent( false ) );

        final RecordingListener listener = new RecordingListener();
        new EventStreamGeneratorImpl( events ).generate( listener );

        assertRecordedEvents( events, listener.getRecordedEvents() );
    }

    @Test
    public void responseWithTwoParams()
        throws XmlRpcException
    {
        final ExtList<Event<?>> events =
            new ExtList<Event<?>>().with( new ResponseEvent( true ) )
                                   .with( new ParameterEvent( 0 ) )
                                   .with( new ValueEvent( "foo", ValueType.STRING ) )
                                   .with( new ParameterEvent( 0, "foo", ValueType.STRING ) )
                                   .with( new ParameterEvent() )
                                   .with( new ParameterEvent( 1 ) )
                                   .with( new ValueEvent( "bar", ValueType.STRING ) )
                                   .with( new ParameterEvent( 1, "bar", ValueType.STRING ) )
                                   .with( new ParameterEvent() )
                                   .with( new ResponseEvent( false ) );

        final RecordingListener listener = new RecordingListener();
        new EventStreamGeneratorImpl( events ).generate( listener );

        assertRecordedEvents( events, listener.getRecordedEvents() );
    }

    @Test
    public void responseWithOneArrayAndOneStructParam()
        throws XmlRpcException
    {
        final ExtList<Event<?>> events =
            new ExtList<Event<?>>().with( new ResponseEvent( true ) )
                                   .with( new ParameterEvent( 0 ) )
                                   .with( new StructEvent( EventType.START_STRUCT ) )
                                   .with( new StructEvent( "sMember" ) )
                                   .with( new ValueEvent( "value", ValueType.STRING ) )
                                   .with( new StructEvent( "sMember", "value", ValueType.STRING ) )
                                   .with( new StructEvent( EventType.END_STRUCT_MEMBER ) )
                                   .with( new StructEvent( "sMember2" ) )
                                   .with( new ValueEvent( "value2", ValueType.STRING ) )
                                   .with( new StructEvent( "sMember2", "value2", ValueType.STRING ) )
                                   .with( new StructEvent( EventType.END_STRUCT_MEMBER ) )
                                   .with( new StructEvent( EventType.END_STRUCT ) )
                                   .with(
                                          new ParameterEvent( 0, new ExtMap<String, String>().with( "sMember", "value" )
                                                                                             .with( "sMember2",
                                                                                                    "value2" ),
                                                              ValueType.STRUCT ) )
                                   .with( new ParameterEvent() )
                                   .with( new ParameterEvent( 1 ) )
                                   .with( new ArrayEvent( EventType.START_ARRAY ) )
                                   .with( new ArrayEvent( 0 ) )
                                   .with( new ValueEvent( "aValue", ValueType.STRING ) )
                                   .with( new ArrayEvent( 0, "aValue", ValueType.STRING ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) )
                                   .with( new ArrayEvent( 1 ) )
                                   .with( new ValueEvent( "aValue2", ValueType.STRING ) )
                                   .with( new ArrayEvent( 1, "aValue2", ValueType.STRING ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) )
                                   .with( new ArrayEvent( EventType.END_ARRAY ) )
                                   .with(
                                          new ParameterEvent( 1, new ExtList<String>().with( "aValue" )
                                                                                      .with( "aValue2" ),
                                                              ValueType.ARRAY ) )
                                   .with( new ParameterEvent() )
                                   .with( new ResponseEvent( false ) );

        final RecordingListener listener = new RecordingListener();
        new EventStreamGeneratorImpl( events ).generate( listener );

        assertRecordedEvents( events, listener.getRecordedEvents() );
    }

}
