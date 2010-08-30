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
