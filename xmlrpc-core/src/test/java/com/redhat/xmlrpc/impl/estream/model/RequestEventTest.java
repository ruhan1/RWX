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

package com.redhat.xmlrpc.impl.estream.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

public class RequestEventTest
    extends AbstractEventTest<Void>
{

    @Test
    public void matchStartRequest()
    {
        assertEquals( EventType.START_REQUEST, new RequestEvent( true ).getEventType() );
        assertEvent( new RequestEvent( true ), EventType.START_REQUEST, null, null, null, true );
    }

    @Test
    public void matchEndRequest()
    {
        assertEquals( EventType.END_REQUEST, new RequestEvent( false ).getEventType() );
        assertEvent( new RequestEvent( false ), EventType.END_REQUEST, null, null, null, true );
    }

    @Test
    public void matchRequestMethod()
    {
        final RequestEvent event = new RequestEvent( "foo" );

        assertEquals( EventType.REQUEST_METHOD, event.getEventType() );
        assertEquals( "foo", event.getMethod() );
        assertEvent( event, EventType.REQUEST_METHOD, null, "foo", ValueType.STRING, true );
    }

    @Test
    public void matchEventEqualsOnStartRequest()
    {
        assertEvent( new RequestEvent( true ), EventType.START_REQUEST, null, null, null, true );
    }

    @Test
    public void matchEventEqualsOnEndRequest()
    {
        assertEvent( new RequestEvent( false ), EventType.END_REQUEST, null, null, null, true );
    }

    @Test
    public void matchEventEqualsOnRequestMethod()
    {
        assertEvent( new RequestEvent( "foo" ), EventType.REQUEST_METHOD, null, "foo", ValueType.STRING, true );
    }

    @Test
    public void matchEventEqualsWithMethodEquals()
    {
        final RequestEvent event = new RequestEvent( "foo" );
        final boolean mEq = event.requestMethodEquals( "foo" );
        final boolean eq = event.eventEquals( EventType.REQUEST_METHOD, null, "foo", ValueType.STRING );

        assertEquals( mEq, eq );
    }

    @Test
    public void mismatchEventEqualsOnStartRequest()
    {
        final RequestEvent event = new RequestEvent( true );

        assertEvent( event, EventType.END_REQUEST, null, null, null, false );
    }

    @Test
    public void mismatchEventEqualsOnEndRequest()
    {
        final RequestEvent event = new RequestEvent( false );

        assertEvent( event, EventType.START_REQUEST, null, null, null, false );
    }

    @Test
    public void mismatchEventEqualsOnRequestMethod()
    {
        final RequestEvent event = new RequestEvent( "foo" );

        assertEvent( event, EventType.END_REQUEST, null, null, null, false );
        assertEvent( event, EventType.REQUEST_METHOD, null, "bar", null, false );
    }

}
