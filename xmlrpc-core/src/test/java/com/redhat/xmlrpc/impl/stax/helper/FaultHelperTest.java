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

package com.redhat.xmlrpc.impl.stax.helper;

import static com.redhat.xmlrpc.impl.estream.testutil.EventAssertions.assertRecordedEvents;

import org.jdom.JDOMException;
import org.junit.Test;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.ResponseEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.impl.estream.testutil.RecordedEvent;
import com.redhat.xmlrpc.impl.estream.testutil.RecordingListener;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.util.List;

public class FaultHelperTest
    extends AbstractStaxHelperTest
{

    @Test
    public void simpleFault()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleFault" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        FaultHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check = new ExtList<Event<?>>( new ResponseEvent( 101, "Test fault" ) );

        assertRecordedEvents( check, events );
    }

}
