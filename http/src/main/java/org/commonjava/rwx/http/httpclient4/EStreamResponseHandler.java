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
package org.commonjava.rwx.http.httpclient4;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.impl.estream.EventStreamParserImpl;
import org.commonjava.rwx.impl.stax.StaxParser;
import org.commonjava.rwx.spi.FaultAwareWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class EStreamResponseHandler
    implements ResponseHandler<List<Event<?>>>
{

    private XmlRpcException error;

    public void throwExceptions()
        throws XmlRpcException
    {
        if ( error != null )
        {
            throw error;
        }
    }

    @Override
    public List<Event<?>> handleResponse( final HttpResponse resp )
        throws ClientProtocolException, IOException
    {
        final StatusLine status = resp.getStatusLine();
        System.out.println( status );
        if ( status.getStatusCode() > 199 && status.getStatusCode() < 203 )
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy( resp.getEntity().getContent(), baos );

            Logger logger = LoggerFactory.getLogger( getClass() );

            File recording = null;
            FileOutputStream stream = null;
            try
            {
                recording = File.createTempFile( "xml-rpc.response.", ".xml" );
                stream = new FileOutputStream( recording );
                stream.write( baos.toByteArray() );
            }
            catch ( final IOException e )
            {
                logger.debug( "Failed to record xml-rpc response to file.", e );
                // this is an auxilliary function. ignore errors.
            }
            finally
            {
                IOUtils.closeQuietly( stream );
                logger.info( "\n\n\nRecorded response to: {}\n\n\n", recording );
            }

            try
            {
                logger.trace( "Got response: \n\n{}", new Object()
                {
                    @Override
                    public String toString()
                    {
                        try
                        {
                            return new String( baos.toByteArray(), "UTF-8" );
                        }
                        catch ( final UnsupportedEncodingException e )
                        {
                            return new String( baos.toByteArray() );
                        }
                    }
                } );

                final EventStreamParserImpl estream = new EventStreamParserImpl();
                new StaxParser( new ByteArrayInputStream( baos.toByteArray() ) ).parse( new FaultAwareWrapper( estream ) );

                return estream.getEvents();
            }
            catch ( final XmlRpcException e )
            {
                error = e;
                return null;
            }
        }
        else
        {
            error = new XmlRpcException( "Invalid response status: '" + status + "'." );
            return null;
        }
    }

}
