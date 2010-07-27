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

package org.commonjava.rwx.http.httpclient4;

import static org.commonjava.rwx.util.LogUtil.info;
import static org.commonjava.rwx.util.LogUtil.trace;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.log4j.Logger;
import org.commonjava.rwx.binding.spi.Bindery;
import org.commonjava.rwx.error.XmlRpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class XmlRpcResponseHandler<T>
    implements ResponseHandler<T>
{

    private static final Logger LOGGER = Logger.getLogger( XmlRpcResponseHandler.class );

    private XmlRpcException error;

    private final Bindery bindery;

    private final Class<T> responseType;

    public XmlRpcResponseHandler( final Bindery bindery, final Class<T> responseType )
    {
        this.bindery = bindery;
        this.responseType = responseType;
    }

    public void throwExceptions()
        throws XmlRpcException
    {
        if ( error != null )
        {
            throw error;
        }
    }

    @Override
    public T handleResponse( final HttpResponse resp )
        throws ClientProtocolException, IOException
    {
        final StatusLine status = resp.getStatusLine();
        System.out.println( status );
        if ( status.getStatusCode() > 199 && status.getStatusCode() < 203 )
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy( resp.getEntity().getContent(), baos );

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
                // this is an auxilliary function. ignore errors.
            }
            finally
            {
                IOUtils.closeQuietly( stream );
                info( LOGGER, "\n\n\nRecorded response to: %s\n\n\n", recording );
            }

            try
            {
                trace( LOGGER, "Got response: \n\n%s", new Object()
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

                return bindery.parse( new ByteArrayInputStream( baos.toByteArray() ), responseType );
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
