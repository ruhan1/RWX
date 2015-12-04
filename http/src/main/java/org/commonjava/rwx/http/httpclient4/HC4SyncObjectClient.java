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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.commonjava.rwx.binding.VoidResponse;
import org.commonjava.rwx.binding.spi.Bindery;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.http.RequestModifier;
import org.commonjava.rwx.http.SyncObjectClient;
import org.commonjava.rwx.http.UrlBuilder;
import org.commonjava.rwx.http.error.XmlRpcTransportException;
import org.commonjava.util.jhttpc.HttpFactory;
import org.commonjava.util.jhttpc.JHttpCException;
import org.commonjava.util.jhttpc.model.SiteConfig;
import org.commonjava.util.jhttpc.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;

import static org.commonjava.rwx.binding.anno.AnnotationUtils.getRequestMethod;

public class HC4SyncObjectClient
        implements SyncObjectClient
{

    private final Bindery bindery;

    private final HttpFactory httpFactory;

    private final SiteConfig siteConfig;

    private final String[] extraPath;

    public HC4SyncObjectClient( final HttpFactory httpFactory, final Bindery bindery, final SiteConfig siteConfig,
                                String... extraPath )
    {
        this.httpFactory = httpFactory;
        this.siteConfig = siteConfig;
        this.extraPath = extraPath;
        this.bindery = bindery;
    }

    @Override
    public <T> T call( final Object request, final Class<T> responseType )
            throws XmlRpcException
    {
        return call( request, responseType, null, null );
    }

    @Override
    public <T> T call( final Object request, final Class<T> responseType, final UrlBuilder urlBuilder,
                       final RequestModifier requestModifier )
            throws XmlRpcException
    {
        final String methodName = getRequestMethod( request );
        if ( methodName == null )
        {
            throw new XmlRpcTransportException( "Request value is not annotated with @Request.", request );
        }

        final HttpPost method;
        try
        {
            String url = UrlUtils.buildUrl( siteConfig.getUri(), extraPath );
            if ( urlBuilder != null )
            {
                url = urlBuilder.buildUrl( url );
            }

            method = new HttpPost( url );
            method.setHeader( "Content-Type", "text/xml" );

            if ( requestModifier != null )
            {
                requestModifier.modifyRequest( method );
            }

            // TODO: Can't we get around pre-rendering to string?? Maybe not, if we want content-length to be right...
            final String content = bindery.renderString( request );

            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.trace( "Sending request:\n\n" + content + "\n\n" );

            method.setEntity( new StringEntity( content ) );
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, request, e );
        }
        catch ( MalformedURLException e )
        {
            throw new XmlRpcTransportException( "Failed to construct URL from: %s and extra-path: %s. Reason: %s", e,
                                                siteConfig.getUri(), Arrays.asList( extraPath ), e.getMessage() );
        }

        CloseableHttpClient client = null;
        try
        {
            client = httpFactory.createClient( siteConfig );

            if ( Void.class.equals( responseType ) )
            {
                final ObjectResponseHandler<VoidResponse> handler =
                        new ObjectResponseHandler<VoidResponse>( bindery, VoidResponse.class );
                client.execute( method, handler );

                handler.throwExceptions();
                return null;
            }
            else
            {
                final ObjectResponseHandler<T> handler = new ObjectResponseHandler<T>( bindery, responseType );
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
        catch ( JHttpCException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, request, e );
        }
        finally
        {
            IOUtils.closeQuietly( client );
        }
    }

    @Override
    public void close()
    {
        IOUtils.closeQuietly( httpFactory );
    }
}
