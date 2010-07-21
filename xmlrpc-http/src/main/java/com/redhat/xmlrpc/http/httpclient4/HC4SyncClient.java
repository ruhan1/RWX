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

package com.redhat.xmlrpc.http.httpclient4;

import static com.redhat.xmlrpc.binding.anno.AnnotationUtils.getRequestMethod;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.redhat.xmlrpc.binding.Bindery;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.http.SyncXmlRpcClient;
import com.redhat.xmlrpc.http.error.XmlRpcTransportException;
import com.redhat.xmlrpc.http.util.FinalHolder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HC4SyncClient
    implements SyncXmlRpcClient
{

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

            method.setHeader( "Content-Length", Integer.toString( content.length() ) );

            method.setEntity( new StringEntity( content ) );
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new XmlRpcTransportException( "Call failed: " + methodName, request, e );
        }

        try
        {
            final FinalHolder<XmlRpcException> errorHolder = new FinalHolder<XmlRpcException>();
            final T response = client.execute( method, new ResponseHandler<T>()
            {
                @Override
                public T handleResponse( final HttpResponse resp )
                    throws ClientProtocolException, IOException
                {
                    final StatusLine status = resp.getStatusLine();
                    if ( status.getStatusCode() > 199 && status.getStatusCode() < 203 )
                    {
                        try
                        {
                            return bindery.parse( resp.getEntity().getContent(), responseType );
                        }
                        catch ( final XmlRpcException e )
                        {
                            errorHolder.setValue( e );
                            return null;
                        }
                    }
                    else
                    {
                        errorHolder.setValue( new XmlRpcException( "Invalid response status: '" + status + "'." ) );
                        return null;
                    }
                }

            } );

            if ( responseType == null && errorHolder.hasValue() )
            {
                throw errorHolder.getValue();
            }

            return response;
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
