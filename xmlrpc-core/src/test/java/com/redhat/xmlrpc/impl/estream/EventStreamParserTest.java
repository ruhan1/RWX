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

import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.RequestEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;

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

}
