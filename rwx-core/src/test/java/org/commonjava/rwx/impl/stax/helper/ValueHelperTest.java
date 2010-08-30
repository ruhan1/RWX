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

public class ValueHelperTest
    extends AbstractStaxHelperTest
{

    @Test
    public void simpleString()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleStringValue" );
        gotoElement( reader );

        final ValueHelper helper = new ValueHelper();

        final RecordingListener listener = new RecordingListener();
        helper.parse( reader, new TrackingXmlRpcListener( listener ) );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check = new ExtList<Event<?>>( new ValueEvent( "foo", ValueType.STRING ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleStructValue()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "structInValue" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        new ValueHelper().parse( reader, new TrackingXmlRpcListener( listener ) );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new StructEvent( EventType.START_STRUCT ), new StructEvent( "key" ),
                                   new ValueEvent( "foo", ValueType.STRING ), new StructEvent( "key", "foo",
                                                                                               ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ),
                                   new ValueEvent( new ExtMap<String, String>( "key", "foo" ), ValueType.STRUCT ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleArrayValue()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "arrayInValue" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        new ValueHelper().parse( reader, new TrackingXmlRpcListener( listener ) );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ArrayEvent( EventType.START_ARRAY ), new ArrayEvent( 0 ),
                                   new ValueEvent( "foo", ValueType.STRING ), new ArrayEvent( 0, "foo",
                                                                                              ValueType.STRING ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ),
                                   new ArrayEvent( EventType.END_ARRAY ), new ValueEvent( new ExtList<String>( "foo" ),
                                                                                          ValueType.ARRAY ) );

        assertRecordedEvents( check, events );
    }

}
