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

package org.commonjava.rwx.binding.internal.xbr;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponseWithFinalFields;
import org.commonjava.rwx.binding.testutil.InheritedPersonRequest;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleAddressMapResponse;
import org.commonjava.rwx.binding.testutil.SimpleConverterRequest;
import org.commonjava.rwx.binding.testutil.SimpleFinalFieldAddress;
import org.commonjava.rwx.binding.testutil.SimpleListRequest;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.binding.testutil.TestObject;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.impl.estream.EventStreamGeneratorImpl;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XBeanRenderingBinderyTest
{

    @Test
    public void simpleRequest()
        throws XmlRpcException
    {
        assertBindings( new SimplePersonRequest() );
    }

    @Test
    public void simpleConverterRequest()
        throws XmlRpcException, ParseException
    {
        assertBindings( new SimpleConverterRequest() );
    }

    @Test
    public void simpleListRequest()
        throws XmlRpcException
    {
        assertBindings( new SimpleListRequest() );
    }

    @Test
    public void simpleAddressMapResponse()
        throws XmlRpcException
    {
        assertBindings( new SimpleAddressMapResponse() );
    }

    @Test( expected = BindException.class )
    public void invalidEntryPoint()
        throws XmlRpcException
    {
        final SimpleAddress address = new SimpleAddress();
        final XBeanRenderingBindery bindery = new XBeanRenderingBindery( address.recipes() );
        bindery.parse( new EventStreamGeneratorImpl( address.events() ), SimpleAddress.class );
    }

    @Test( expected = XmlRpcFaultException.class )
    public void fault()
        throws XmlRpcException
    {
        final XBeanRenderingBindery bindery = new XBeanRenderingBindery( new SimplePersonRequest().recipes() );

        final List<Event<?>> events = new ArrayList<Event<?>>(
                Arrays.asList( new ResponseEvent( true ), new ResponseEvent( 101, "test fault" ),
                               new ResponseEvent( false ) ) );

        bindery.parse( new EventStreamGeneratorImpl( events ), SimplePersonRequest.class );
    }

    @Test
    public void requestWithInheritedFields()
        throws XmlRpcException
    {
        assertBindings( new InheritedPersonRequest() );
    }

    @Test
    public void responseWithStructParam()
        throws XmlRpcException
    {
        assertBindings( new ComposedPersonResponse() );
    }

    @Test
    public void responseWithArrayParam()
        throws XmlRpcException
    {
        assertBindings( new ComposedPersonResponse3() );
    }

    @Test
    public void responseWithConstructorAnnotations()
        throws XmlRpcException
    {
        assertBindings( new ComposedPersonResponseWithFinalFields( "foo", "foo@nowhere.com",
                                                                   new SimpleFinalFieldAddress( "123 Sesame St",
                                                                                                "Little Big Fork",
                                                                                                "NV", "01234" ) ) );
    }

    @SuppressWarnings( "unchecked" )
    private <T extends TestObject> void assertBindings( final T object )
        throws XmlRpcException
    {
        final XBeanRenderingBindery bindery = new XBeanRenderingBindery( object.recipes() );
        final T result = (T) bindery.parse( new EventStreamGeneratorImpl( object.events() ), object.getClass() );

        assertEquals( object, result );
    }

}
