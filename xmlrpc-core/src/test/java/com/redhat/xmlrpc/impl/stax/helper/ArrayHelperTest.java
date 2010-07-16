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
import com.redhat.xmlrpc.impl.estream.model.ArrayEvent;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.StructEvent;
import com.redhat.xmlrpc.impl.estream.model.ValueEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.impl.estream.testutil.ExtMap;
import com.redhat.xmlrpc.impl.estream.testutil.RecordedEvent;
import com.redhat.xmlrpc.impl.estream.testutil.RecordingListener;
import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.util.List;

public class ArrayHelperTest
    extends AbstractStaxHelperTest
{

    @Test
    public void simpleOneElementArray()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleOneElementArray" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        ArrayHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ArrayEvent( EventType.START_ARRAY ), new ArrayEvent( 0 ),
                                   new ValueEvent( "foo", ValueType.STRING ), new ArrayEvent( 0, "foo",
                                                                                              ValueType.STRING ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ), new ArrayEvent( EventType.END_ARRAY ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleTwoElementArray()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleTwoElementArray" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        ArrayHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ArrayEvent( EventType.START_ARRAY ), new ArrayEvent( 0 ),
                                   new ValueEvent( "foo", ValueType.STRING ), new ArrayEvent( 0, "foo",
                                                                                              ValueType.STRING ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ), new ArrayEvent( 1 ),
                                   new ValueEvent( "foo2", ValueType.STRING ), new ArrayEvent( 1, "foo2",
                                                                                               ValueType.STRING ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ), new ArrayEvent( EventType.END_ARRAY ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void structEmbeddedInArray()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "structInArray" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        ArrayHelper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final ExtMap<String, String> map = new ExtMap<String, String>( "key", "foo" );

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ArrayEvent( EventType.START_ARRAY ), new ArrayEvent( 0 ),
                                   new StructEvent( EventType.START_STRUCT ), new StructEvent( "key" ),
                                   new ValueEvent( "foo", ValueType.STRING ), new StructEvent( "key", "foo",
                                                                                               ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ), new ValueEvent( map, ValueType.STRUCT ),
                                   new ArrayEvent( 0, map, ValueType.STRUCT ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ), new ArrayEvent( EventType.END_ARRAY ) );

        assertRecordedEvents( check, events );
    }

}
