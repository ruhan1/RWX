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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamGenerator;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.http.RequestModifier;
import org.commonjava.rwx.http.SyncEStreamClient;
import org.commonjava.rwx.http.UrlBuilder;
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

    public HC4SyncEStreamClient( final HttpFactory httpFactory, final SiteConfig siteConfig, String... extraPath )
    {
        this.httpFactory = httpFactory;
        this.siteConfig = siteConfig;
        this.extraPath = extraPath;
    }

    @Override
    public List<Event<?>> call( final List<Event<?>> events, final boolean expectVoidResponse )
            throws XmlRpcException
    {
        return call( events, expectVoidResponse, null, null );
    }

    @Override
    public List<Event<?>> call( final List<Event<?>> events, final boolean expectVoidResponse, UrlBuilder urlBuilder,
                                RequestModifier requestModifier )
            throws XmlRpcException
    {
        return call( new EventStreamGeneratorImpl( events ), expectVoidResponse, urlBuilder, requestModifier );
    }

    @Override
    public List<Event<?>> call( final EventStreamGenerator requestGenerator, final boolean expectVoidResponse )
            throws XmlRpcException
    {
        return call( requestGenerator, expectVoidResponse, null, null );
    }

    @Override
    public List<Event<?>> call( final EventStreamGenerator requestGenerator, final boolean expectVoidResponse,
                                UrlBuilder urlBuilder, RequestModifier requestModifier )
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
