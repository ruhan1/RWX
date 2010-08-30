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
import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
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
        StructHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

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
        StructHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

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
        StructHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

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
