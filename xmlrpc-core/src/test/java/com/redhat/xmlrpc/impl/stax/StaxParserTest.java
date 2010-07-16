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

package com.redhat.xmlrpc.impl.stax;

import static com.redhat.xmlrpc.impl.estream.testutil.EventAssertions.assertRecordedEvents;

import org.jdom.JDOMException;
import org.junit.Test;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.RequestEvent;
import com.redhat.xmlrpc.impl.estream.model.ResponseEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.impl.estream.testutil.RecordedEvent;
import com.redhat.xmlrpc.impl.estream.testutil.RecordingListener;

import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.util.List;

public class StaxParserTest
    extends AbstractStaxTest
{

    @Test
    public void simpleResponse_InputStream()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLStream( "simpleResponse" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ResponseEvent( true ), new ResponseEvent( 101, "foo" ),
                                   new ResponseEvent( false ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleResponse_Reader()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLReader( "simpleResponse" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ResponseEvent( true ), new ResponseEvent( 101, "foo" ),
                                   new ResponseEvent( false ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleResponse_String()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLString( "simpleResponse" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ResponseEvent( true ), new ResponseEvent( 101, "foo" ),
                                   new ResponseEvent( false ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleRequest_InputStream()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLStream( "simpleRequest" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new RequestEvent( true ), new RequestEvent( "foo" ), new RequestEvent( false ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleRequest_Reader()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLReader( "simpleRequest" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new RequestEvent( true ), new RequestEvent( "foo" ), new RequestEvent( false ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleRequest_String()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLString( "simpleRequest" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new RequestEvent( true ), new RequestEvent( "foo" ), new RequestEvent( false ) );

        assertRecordedEvents( check, events );
    }

}
