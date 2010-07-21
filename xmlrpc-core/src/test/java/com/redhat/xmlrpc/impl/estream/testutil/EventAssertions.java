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

package com.redhat.xmlrpc.impl.estream.testutil;

import static org.junit.Assert.fail;

import com.redhat.xmlrpc.impl.estream.model.Event;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class EventAssertions
{

    private EventAssertions()
    {
    }

    @SuppressWarnings( "unchecked" )
    public static void assertEvents( final List<Event<? extends Object>> check, final List<Event<?>> recorded )
    {
        if ( check.size() != recorded.size() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "Event-list sizes differ. Real events: " ).append( check.size() ).append( ":\n" );

            for ( int i = 0; i < check.size(); i++ )
            {
                final Event<?> e = check.get( i );
                sb.append( "\n\t" ).append( i ).append( ". " ).append( e );
            }

            sb.append( "\n\nSimulated events: " ).append( recorded.size() ).append( ":\n" );
            for ( int i = 0; i < recorded.size(); i++ )
            {
                final Event<?> e = recorded.get( i );
                sb.append( "\n\t" ).append( i ).append( ". " ).append( e );
            }

            fail( sb.toString() );
        }

        final List<Integer> failures = new ArrayList<Integer>();
        for ( int i = 0; i < check.size(); i++ )
        {
            final Event<Object> evt = (Event<Object>) check.get( i );
            final Event<Object> re = (Event<Object>) recorded.get( i );
            if ( !evt.equals( re ) )
            {
                failures.add( i );
            }
        }

        if ( !failures.isEmpty() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "The following indexes in the event-list did not match the recorded values:" );
            for ( final Integer idx : failures )
            {
                sb.append( "\n[" )
                  .append( idx )
                  .append( "]\n\tReal: " )
                  .append( check.get( idx ) )
                  .append( "\n\tSimulated: " )
                  .append( recorded.get( idx ) )
                  .append( "\n" );
            }

            fail( sb.toString() );
        }
    }

    @SuppressWarnings( "unchecked" )
    public static void assertRecordedEvents( final List<Event<? extends Object>> check,
                                             final List<RecordedEvent> recorded )
    {
        if ( check.size() != recorded.size() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "Event-list sizes differ. Expected events: " ).append( check.size() ).append( ":\n" );

            for ( int i = 0; i < check.size(); i++ )
            {
                final Event<?> e = check.get( i );
                sb.append( "\n\t" ).append( i ).append( ". " ).append( e );
            }

            sb.append( "\n\nRecorded events: " ).append( recorded.size() ).append( ":\n" );
            for ( int i = 0; i < recorded.size(); i++ )
            {
                final RecordedEvent e = recorded.get( i );
                sb.append( "\n\t" ).append( i ).append( ". " ).append( e );
            }

            fail( sb.toString() );
        }

        final List<Integer> failures = new ArrayList<Integer>();
        for ( int i = 0; i < check.size(); i++ )
        {
            final Event<Object> evt = (Event<Object>) check.get( i );
            final RecordedEvent re = recorded.get( i );
            if ( !evt.eventEquals( re.getEventType(), re.getKey(), re.getValue(), re.getValueType() ) )
            {
                failures.add( i );
            }
        }

        if ( !failures.isEmpty() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "The following indexes in the event-list did not match the recorded values:" );
            for ( final Integer idx : failures )
            {
                sb.append( "\n[" )
                  .append( idx )
                  .append( "]\n\tExpected: " )
                  .append( check.get( idx ) )
                  .append( "\n\tActual: " )
                  .append( recorded.get( idx ) )
                  .append( "\n" );
            }

            fail( sb.toString() );
        }
    }

    public static void assertRecordedToRecorded( final List<RecordedEvent> check, final List<RecordedEvent> recorded )
    {
        if ( check == null && recorded == null )
        {
            return;
        }
        else if ( check == null && recorded != null )
        {
            fail( "Expected events were null, but Actual events were NOT null." );
        }
        else if ( check != null && recorded == null )
        {
            fail( "Expected events were null, but Actual events were NOT null." );
        }

        if ( check.size() != recorded.size() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "Event-list sizes differ. Expected events: " ).append( check.size() ).append( ":\n" );

            for ( int i = 0; i < check.size(); i++ )
            {
                final RecordedEvent e = check.get( i );
                sb.append( "\n\t" ).append( i ).append( ". " ).append( e );
            }

            sb.append( "\n\nRecorded events: " ).append( recorded.size() ).append( ":\n" );
            for ( int i = 0; i < recorded.size(); i++ )
            {
                final RecordedEvent e = recorded.get( i );
                sb.append( "\n\t" ).append( i ).append( ". " ).append( e );
            }

            sb.append( "\n\nLeftover events: " );

            Set<RecordedEvent> leftover;
            if ( check.size() > recorded.size() )
            {
                leftover = new LinkedHashSet<RecordedEvent>( check );
                leftover.removeAll( recorded );
            }
            else
            {
                leftover = new LinkedHashSet<RecordedEvent>( recorded );
                leftover.removeAll( check );
            }

            int i = 0;
            for ( final RecordedEvent e : leftover )
            {
                sb.append( "\n\t" ).append( i++ ).append( ". " ).append( e );
            }

            fail( sb.toString() );
        }

        final List<Integer> failures = new ArrayList<Integer>();
        for ( int i = 0; i < check.size(); i++ )
        {
            final RecordedEvent evt = check.get( i );
            final RecordedEvent re = recorded.get( i );
            if ( !evt.equals( re ) )
            {
                failures.add( i );
            }
        }

        if ( !failures.isEmpty() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "The following indexes in the event-list did not match the recorded values:" );
            for ( final Integer idx : failures )
            {
                sb.append( "\n[" )
                  .append( idx )
                  .append( "]\n\tExpected: " )
                  .append( check.get( idx ) )
                  .append( "\n\tActual: " )
                  .append( recorded.get( idx ) )
                  .append( "\n" );
            }

            fail( sb.toString() );
        }
    }

}
