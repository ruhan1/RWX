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
