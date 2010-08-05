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
import org.commonjava.rwx.impl.estream.model.Event;
import org.commonjava.rwx.impl.estream.model.ResponseEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.RecordedEvent;
import org.commonjava.rwx.impl.estream.testutil.RecordingListener;
import org.jdom.JDOMException;
import org.junit.Test;

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
        FaultHelper.parse( reader, new TrackingXmlRpcListener( listener ) );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check = new ExtList<Event<?>>( new ResponseEvent( 101, "Test fault" ) );

        assertRecordedEvents( check, events );
    }

}
