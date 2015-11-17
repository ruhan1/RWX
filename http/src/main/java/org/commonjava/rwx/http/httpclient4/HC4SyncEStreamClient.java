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
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamGenerator;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.http.SyncEStreamClient;
import org.commonjava.rwx.http.error.XmlRpcTransportException;
import org.commonjava.rwx.impl.estream.EventStreamGeneratorImpl;
import org.commonjava.rwx.impl.jdom.JDomRenderer;
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
import java.util.List;

import static org.commonjava.rwx.estream.EStreamUtils.getRequestMethod;

public class HC4SyncEStreamClient
    implements SyncEStreamClient
{

    private HttpFactory httpFactory;

    private SiteConfig siteConfig;

    private String[] extraPath;

    public HC4SyncEStreamClient( final HttpFactory httpFactory, final SiteConfig siteConfig, String...extraPath )
    {
        this.httpFactory = httpFactory;
        this.siteConfig = siteConfig;
        this.extraPath = extraPath;
    }

    @Override
    public List<Event<?>> call( final List<Event<?>> events, final boolean expectVoidResponse )
        throws XmlRpcException
    {
        return call( new EventStreamGeneratorImpl( events ), expectVoidResponse );
    }

    @Override
    public List<Event<?>> call( final EventStreamGenerator requestGenerator, final boolean expectVoidResponse )
        throws XmlRpcException
    {
        final List<Event<?>> events = requestGenerator.getEvents();

        final String methodName = getRequestMethod( events );
        if ( methodName == null )
        {
            throw new XmlRpcTransportException( "Request value is not annotated with @Request.", events );
        }

        Logger logger = LoggerFactory.getLogger( getClass() );
        final HttpPost method;
        try
        {
            method = new HttpPost( UrlUtils.buildUrl( siteConfig.getUri(), extraPath ) );
            method.setHeader( "Content-Type", "text/xml" );

            // TODO: Can't we get around pre-rendering to string?? Maybe not, if we want content-length to be right...
            final JDomRenderer renderer = new JDomRenderer();
            requestGenerator.generate( renderer );

            final String content = renderer.documentToString();
            logger.trace( "Sending request:\n\n" + content + "\n\n" );

            method.setEntity( new StringEntity( content ) );
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, events, e );
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

            if ( expectVoidResponse )
            {
                final EStreamResponseHandler handler = new EStreamResponseHandler();
                client.execute( method, handler );

                handler.throwExceptions();
                return null;
            }
            else
            {
                final EStreamResponseHandler handler = new EStreamResponseHandler();
                final List<Event<?>> responseEvents = client.execute( method, handler );

                handler.throwExceptions();
                return responseEvents;
            }
        }
        catch ( final ClientProtocolException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, events, e );
        }
        catch ( final IOException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, events, e );
        }
        catch ( JHttpCException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, events, e );
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
