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
package org.commonjava.rwx.impl.estream.model;

import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtListOfArrays;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.util.List;

public class ResponseEventTest
    extends AbstractEventTest<Integer>
{

    @Test
    public void matchStartResponse()
    {
        assertEquals( EventType.START_RESPONSE, new ResponseEvent( true ).getEventType() );
        assertEvent( new ResponseEvent( true ), EventType.START_RESPONSE, null, null, null, true );
    }

    @Test
    public void matchEndResponse()
    {
        assertEquals( EventType.END_RESPONSE, new ResponseEvent( false ).getEventType() );
        assertEvent( new ResponseEvent( false ), EventType.END_RESPONSE, null, null, null, true );
    }

    @Test
    public void matchResponseFaultInfo()
    {
        final ResponseEvent event = new ResponseEvent( 999, "Test fault" );

        assertEquals( EventType.FAULT, event.getEventType() );
        assertEquals( 999, event.getCode() );
        assertEquals( "Test fault", event.getMessage() );
        assertEvent( event, EventType.FAULT, 999, "Test fault", ValueType.STRING, true );
    }

    @Test
    public void matchResponseEqualsAndEventEqualsForFault()
    {
        final ResponseEvent event = new ResponseEvent( 999, "Test fault" );
        final boolean rEq = event.responseFaultEquals( 999, "Test fault" );
        final boolean eq = event.eventEquals( EventType.FAULT, 999, "Test fault", ValueType.STRING );

        assertEquals( rEq, eq );
    }

    @Test
    public void mismatchEventInfo()
    {
        assertEvent( new ResponseEvent( true ), EventType.END_RESPONSE, null, null, null, false );
        assertEvent( new ResponseEvent( false ), EventType.START_RESPONSE, null, null, null, false );

        final ResponseEvent event = new ResponseEvent( 999, "Test message" );

        assertEvent( event, EventType.ARRAY_ELEMENT, 999, "Test message", ValueType.STRING, false );
        assertEvent( event, EventType.FAULT, 998, "Test message", ValueType.STRING, false );
        assertEvent( event, EventType.FAULT, 999, "Other message", ValueType.STRING, false );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<Integer>[]> constructEqualInstanceSets()
    {
        return new ExtListOfArrays<Event<Integer>>( 2 ).with( new ResponseEvent( true ), new ResponseEvent( true ) )
                                                       .with( new ResponseEvent( 100, "Test fault" ),
                                                              new ResponseEvent( 100, "Test fault" ) );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<Integer>[]> constructUnequalInstanceSets()
    {
        return new ExtListOfArrays<Event<Integer>>( 2 ).with( new ResponseEvent( true ), new ResponseEvent( false ) )
                                                       .with( new ResponseEvent( 100, "Test fault" ),
                                                              new ResponseEvent( 101, "Test fault" ) )
                                                       .with( new ResponseEvent( 100, "Test fault" ),
                                                              new ResponseEvent( 100, "Test fault2" ) );
    }

}
