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

package org.commonjava.rwx.impl.stax.helper;

import static org.commonjava.rwx.impl.estream.testutil.EventAssertions.assertRecordedEvents;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.impl.estream.model.ArrayEvent;
import org.commonjava.rwx.impl.estream.model.Event;
import org.commonjava.rwx.impl.estream.model.StructEvent;
import org.commonjava.rwx.impl.estream.model.ValueEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.ExtMap;
import org.commonjava.rwx.impl.estream.testutil.RecordedEvent;
import org.commonjava.rwx.impl.estream.testutil.RecordingListener;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.jdom.JDOMException;
import org.junit.Test;

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
        ArrayHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

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
        ArrayHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

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
        ArrayHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

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
