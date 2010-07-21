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

package org.commonjava.rwx.binding.internal.xbr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.xbr.XBRCompositionBindery;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponseWithFinalFields;
import org.commonjava.rwx.binding.testutil.InheritedPersonRequest;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleFinalFieldAddress;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.impl.estream.EventStreamGenerator;
import org.commonjava.rwx.impl.estream.EventStreamParser;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class XBRCompositionBinderyTest
{

    @Test
    public void simpleRequest()
        throws XmlRpcException
    {
        assertRoundTrip_EventDriven( new SimplePersonRequest() );
        assertRoundTrip_StreamDriven( new SimplePersonRequest() );
        assertRoundTrip_WriterReaderDriven( new SimplePersonRequest() );
        assertRoundTrip_StringDriven( new SimplePersonRequest() );
    }

    @Test( expected = BindException.class )
    public void invalidEntryPoint()
        throws XmlRpcException
    {
        XBRCompositionBindery.binderyFor( SimpleAddress.class );
    }

    @Test
    public void fault()
        throws XmlRpcException
    {
        final XmlRpcFaultException fault = new XmlRpcFaultException( 101, "Test fault" );

        final XBRCompositionBindery bindery = XBRCompositionBindery.binderyFor( SimplePersonRequest.class );

        try
        {
            final EventStreamParser parser = new EventStreamParser();
            bindery.render( parser, fault );

            bindery.parse( new EventStreamGenerator( parser.getEvents() ), SimplePersonRequest.class );
            fail( "Fault was not thrown from bindery.parse()" );
        }
        catch ( final XmlRpcFaultException e )
        {
            assertEquals( fault, e );
        }

        try
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bindery.render( baos, fault );

            bindery.parse( new ByteArrayInputStream( baos.toByteArray() ), SimplePersonRequest.class );
            fail( "Fault was not thrown from bindery.parse()" );
        }
        catch ( final XmlRpcFaultException e )
        {
            assertEquals( fault, e );
        }

        try
        {
            final StringWriter writer = new StringWriter();
            bindery.render( writer, fault );

            bindery.parse( new StringReader( writer.toString() ), SimplePersonRequest.class );
            fail( "Fault was not thrown from bindery.parse()" );
        }
        catch ( final XmlRpcFaultException e )
        {
            assertEquals( fault, e );
        }

        try
        {
            final String xml = bindery.renderString( fault );

            bindery.parse( xml, SimplePersonRequest.class );
            fail( "Fault was not thrown from bindery.parse()" );
        }
        catch ( final XmlRpcFaultException e )
        {
            assertEquals( fault, e );
        }

    }

    @Test
    public void requestWithInheritedFields()
        throws XmlRpcException
    {
        assertRoundTrip_EventDriven( new InheritedPersonRequest() );
        assertRoundTrip_StreamDriven( new InheritedPersonRequest() );
        assertRoundTrip_WriterReaderDriven( new InheritedPersonRequest() );
        assertRoundTrip_StringDriven( new InheritedPersonRequest() );
    }

    @Test
    public void responseWithStructParam()
        throws XmlRpcException
    {
        assertRoundTrip_EventDriven( new ComposedPersonResponse() );
        assertRoundTrip_StreamDriven( new ComposedPersonResponse() );
        assertRoundTrip_WriterReaderDriven( new ComposedPersonResponse() );
        assertRoundTrip_StringDriven( new ComposedPersonResponse() );
    }

    @Test
    public void responseWithArrayParam()
        throws XmlRpcException
    {
        assertRoundTrip_EventDriven( new ComposedPersonResponse3() );
        assertRoundTrip_StreamDriven( new ComposedPersonResponse3() );
        assertRoundTrip_WriterReaderDriven( new ComposedPersonResponse3() );
        assertRoundTrip_StringDriven( new ComposedPersonResponse3() );
    }

    @Test
    public void responseWithConstructorAnnotations()
        throws XmlRpcException
    {
        final SimpleFinalFieldAddress address =
            new SimpleFinalFieldAddress( "123 Sesame St", "Little Big Fork", "NV", "01234" );

        final ComposedPersonResponseWithFinalFields response =
            new ComposedPersonResponseWithFinalFields( "foo", "foo@nowhere.com", address );

        assertRoundTrip_EventDriven( response );
        assertRoundTrip_StreamDriven( response );
        assertRoundTrip_WriterReaderDriven( response );
        assertRoundTrip_StringDriven( response );
    }

    private void assertRoundTrip_EventDriven( final Object object )
        throws XmlRpcException
    {
        final Class<?> type = object.getClass();

        final XBRCompositionBindery bindery = XBRCompositionBindery.binderyFor( type );

        final EventStreamParser parser = new EventStreamParser();
        bindery.render( parser, object );

        final Object result = bindery.parse( new EventStreamGenerator( parser.getEvents() ), type );

        assertEquals( object, result );
    }

    private void assertRoundTrip_StreamDriven( final Object object )
        throws XmlRpcException
    {
        final Class<?> type = object.getClass();

        final XBRCompositionBindery bindery = XBRCompositionBindery.binderyFor( type );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bindery.render( baos, object );

        final Object result = bindery.parse( new ByteArrayInputStream( baos.toByteArray() ), type );

        assertEquals( object, result );
    }

    private void assertRoundTrip_WriterReaderDriven( final Object object )
        throws XmlRpcException
    {
        final Class<?> type = object.getClass();

        final XBRCompositionBindery bindery = XBRCompositionBindery.binderyFor( type );

        final StringWriter writer = new StringWriter();

        bindery.render( writer, object );

        final Object result = bindery.parse( new StringReader( writer.toString() ), type );

        assertEquals( object, result );
    }

    private void assertRoundTrip_StringDriven( final Object object )
        throws XmlRpcException
    {
        final Class<?> type = object.getClass();

        final XBRCompositionBindery bindery = XBRCompositionBindery.binderyFor( type );

        final String xml = bindery.renderString( object );

        final Object result = bindery.parse( xml, type );

        assertEquals( object, result );
    }

}
