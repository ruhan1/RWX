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

import static org.commonjava.rwx.estream.EStreamUtils.getRequestMethod;
import static org.commonjava.rwx.util.LogUtil.trace;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamGenerator;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.http.SyncEStreamClient;
import org.commonjava.rwx.http.error.XmlRpcTransportException;
import org.commonjava.rwx.impl.estream.EventStreamGeneratorImpl;
import org.commonjava.rwx.impl.jdom.JDomRenderer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class HC4SyncEStreamClient
    implements SyncEStreamClient
{

    private static final Logger LOGGER = Logger.getLogger( HC4SyncEStreamClient.class );

    private final HttpClient client;

    private final CharSequence url;

    public HC4SyncEStreamClient( final CharSequence url )
    {
        this.url = url;
        client = new DefaultHttpClient();
    }

    public HC4SyncEStreamClient( final HttpClient client, final CharSequence url )
    {
        this.client = client;
        this.url = url;
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

        final HttpPost method = new HttpPost( url.toString() );
        method.setHeader( "Content-Type", "text/xml" );
        try
        {
            // TODO: Can't we get around pre-rendering to string?? Maybe not, if we want content-length to be right...
            final JDomRenderer renderer = new JDomRenderer();
            requestGenerator.generate( renderer );

            final String content = renderer.documentToString();
            trace( LOGGER, "Sending request:\n\n" + content + "\n\n" );

            method.setEntity( new StringEntity( content ) );
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, events, e );
        }

        try
        {
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
    }

}
