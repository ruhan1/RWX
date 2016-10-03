/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.binding.internal.xbr;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponseWithFinalFields;
import org.commonjava.rwx.binding.testutil.DoubleRequest;
import org.commonjava.rwx.binding.testutil.InheritedPersonRequest;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleFinalFieldAddress;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.impl.estream.EventStreamGeneratorImpl;
import org.commonjava.rwx.impl.estream.EventStreamParserImpl;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
            final EventStreamParserImpl parser = new EventStreamParserImpl();
            bindery.render( parser, fault );

            bindery.parse( new EventStreamGeneratorImpl( parser.getEvents() ), SimplePersonRequest.class );
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

    @Test
    public void responseWithConstructorAnnotations_Stream()
            throws XmlRpcException
    {
        final SimpleFinalFieldAddress address =
                new SimpleFinalFieldAddress( "123 Sesame St", "Little Big Fork", "NV", "01234" );

        final ComposedPersonResponseWithFinalFields response =
                new ComposedPersonResponseWithFinalFields( "foo", "foo@nowhere.com", address );

        assertRoundTrip_StreamDriven( response );
    }

    @Test
    public void doubleResponse() throws XmlRpcException {
        DoubleRequest intResponse = new DoubleRequest(3.0);

        assertRoundTrip_StreamDriven(intResponse);
    }

    private void assertRoundTrip_EventDriven( final Object object )
        throws XmlRpcException
    {
        final Class<?> type = object.getClass();

        final XBRCompositionBindery bindery = XBRCompositionBindery.binderyFor( type );

        final EventStreamParserImpl parser = new EventStreamParserImpl();
        bindery.render( parser, object );

        final Object result = bindery.parse( new EventStreamGeneratorImpl( parser.getEvents() ), type );

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
