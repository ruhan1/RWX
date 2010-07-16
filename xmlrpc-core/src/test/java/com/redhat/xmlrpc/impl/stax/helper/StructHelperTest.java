package com.redhat.xmlrpc.impl.stax.helper;

import static com.redhat.xmlrpc.impl.estream.testutil.EventAssertions.assertRecordedEvents;

import org.jdom.JDOMException;
import org.junit.Test;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.estream.model.ArrayEvent;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.StructEvent;
import com.redhat.xmlrpc.impl.estream.model.ValueEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.impl.estream.testutil.RecordedEvent;
import com.redhat.xmlrpc.impl.estream.testutil.RecordingListener;
import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.util.List;

public class StructHelperTest
    extends AbstractStaxHelperTest
{

    @Test
    public void simpleOneMemberStruct()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleOneMemberStruct" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        StructHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new StructEvent( EventType.START_STRUCT ), new StructEvent( "key" ),
                                   new ValueEvent( "foo", ValueType.STRING ), new StructEvent( "key", "foo",
                                                                                               ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleTwoMemberStruct()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleTwoMemberStruct" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        StructHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new StructEvent( EventType.START_STRUCT ), new StructEvent( "key" ),
                                   new ValueEvent( "foo", ValueType.STRING ), new StructEvent( "key", "foo",
                                                                                               ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ), new StructEvent( "key2" ),
                                   new ValueEvent( "foo2", ValueType.STRING ), new StructEvent( "key2", "foo2",
                                                                                                ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void arrayEmbeddedInStruct()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "arrayInStruct" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        StructHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new StructEvent( EventType.START_STRUCT ), new StructEvent( "key" ),
                                   new ArrayEvent( EventType.START_ARRAY ), new ArrayEvent( 0 ),
                                   new ValueEvent( "foo", ValueType.STRING ), new ArrayEvent( 0, "foo",
                                                                                              ValueType.STRING ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ),
                                   new ArrayEvent( EventType.END_ARRAY ), new ValueEvent( new ExtList<String>( "foo" ),
                                                                                          ValueType.ARRAY ),
                                   new StructEvent( "key", new ExtList<String>( "foo" ), ValueType.ARRAY ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ) );

        assertRecordedEvents( check, events );
    }

}
