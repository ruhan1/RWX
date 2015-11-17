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
package org.commonjava.rwx.impl.stax;

import static org.commonjava.rwx.impl.estream.testutil.EventAssertions.assertRecordedEvents;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.ExtMap;
import org.commonjava.rwx.impl.estream.testutil.RecordedEvent;
import org.commonjava.rwx.impl.estream.testutil.RecordingListener;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.jdom.JDOMException;
import org.junit.Test;

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

    @Test
    public void jiraServerInfo_String()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final StaxParser parser = new StaxParser( getXMLString( "jiraServerInfo" ) );

        final RecordingListener listener = new RecordingListener();
        parser.parse( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final ExtMap<String, String> map =
            new ExtMap<String, String>( "version", "4.1.2" ).with( "baseUrl", "http://jira.codehaus.org" )
                                                            .with( "buildDate", "Mon Jun 07 00:00:00 CDT 2010" )
                                                            .with( "buildNumber", "531" )
                                                            .with( "edition", "Enterprise" )
                                                            .with( "serverTime",
                                                                   "com.atlassian.jira.rpc.soap.beans.RemoteTimeInfo@18702b7" );

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ResponseEvent( true ), new ParameterEvent( 0 ),
                                   new StructEvent( EventType.START_STRUCT ), new StructEvent( "version" ),
                                   new ValueEvent( "4.1.2", ValueType.STRING ), new StructEvent( "version", "4.1.2",
                                                                                                 ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ), new StructEvent( "baseUrl" ),
                                   new ValueEvent( "http://jira.codehaus.org", ValueType.STRING ),
                                   new StructEvent( "baseUrl", "http://jira.codehaus.org", ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ), new StructEvent( "buildDate" ),
                                   new ValueEvent( "Mon Jun 07 00:00:00 CDT 2010", ValueType.STRING ),
                                   new StructEvent( "buildDate", "Mon Jun 07 00:00:00 CDT 2010", ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ), new StructEvent( "buildNumber" ),
                                   new ValueEvent( "531", ValueType.STRING ), new StructEvent( "buildNumber", "531",
                                                                                               ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ), new StructEvent( "edition" ),
                                   new ValueEvent( "Enterprise", ValueType.STRING ),
                                   new StructEvent( "edition", "Enterprise", ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ), new StructEvent( "serverTime" ),
                                   new ValueEvent( "com.atlassian.jira.rpc.soap.beans.RemoteTimeInfo@18702b7",
                                                   ValueType.STRING ),
                                   new StructEvent( "serverTime",
                                                    "com.atlassian.jira.rpc.soap.beans.RemoteTimeInfo@18702b7",
                                                    ValueType.STRING ), new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ), new ValueEvent( map, ValueType.STRUCT ),
                                   new ParameterEvent( 0, map, ValueType.STRUCT ), new ParameterEvent(),
                                   new ResponseEvent( false ) );

        assertRecordedEvents( check, events );
    }

}
