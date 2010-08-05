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

import static org.commonjava.rwx.binding.anno.AnnotationUtils.getRequestMethod;
import static org.commonjava.rwx.util.LogUtil.trace;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.commonjava.rwx.binding.VoidResponse;
import org.commonjava.rwx.binding.spi.Bindery;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.http.SyncXmlRpcClient;
import org.commonjava.rwx.http.error.XmlRpcTransportException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HC4SyncClient
    implements SyncXmlRpcClient
{

    private static final Logger LOGGER = Logger.getLogger( HC4SyncClient.class );

    private final HttpClient client;

    private final CharSequence url;

    private final Bindery bindery;

    public HC4SyncClient( final CharSequence url, final Bindery bindery )
    {
        this.url = url;
        this.bindery = bindery;
        client = new DefaultHttpClient();
    }

    public HC4SyncClient( final HttpClient client, final CharSequence url, final Bindery bindery )
    {
        this.client = client;
        this.url = url;
        this.bindery = bindery;
    }

    @Override
    public <T> T call( final Object request, final Class<T> responseType )
        throws XmlRpcException
    {
        final String methodName = getRequestMethod( request );
        if ( methodName == null )
        {
            throw new XmlRpcTransportException( "Request value is not annotated with @Request.", request );
        }

        final HttpPost method = new HttpPost( url.toString() );
        method.setHeader( "Content-Type", "text/xml" );
        try
        {
            // TODO: Can't we get around pre-rendering to string?? Maybe not, if we want content-length to be right...
            final String content = bindery.renderString( request );
            trace( LOGGER, "Sending request:\n\n" + content + "\n\n" );

            method.setEntity( new StringEntity( content ) );
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, request, e );
        }

        try
        {
            if ( Void.class.equals( responseType ) )
            {
                final XmlRpcResponseHandler<VoidResponse> handler =
                    new XmlRpcResponseHandler<VoidResponse>( bindery, VoidResponse.class );
                client.execute( method, handler );

                handler.throwExceptions();
                return null;
            }
            else
            {
                final XmlRpcResponseHandler<T> handler = new XmlRpcResponseHandler<T>( bindery, responseType );
                final T response = client.execute( method, handler );

                handler.throwExceptions();
                return response;

            }
        }
        catch ( final ClientProtocolException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, request, e );
        }
        catch ( final IOException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, request, e );
        }
    }

}
