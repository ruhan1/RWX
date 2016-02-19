package org.commonjava.rwx.http;

import org.commonjava.rwx.error.XmlRpcException;

import java.net.MalformedURLException;

/**
 * Created by jdcasey on 2/8/16.
 */
public final class UrlBuildResult
{
    private Throwable error;

    private String url;

    public UrlBuildResult( MalformedURLException error )
    {
        this.error = error;
    }

    public UrlBuildResult( XmlRpcException error )
    {
        this.error = error;
    }

    public UrlBuildResult( String url )
    {
        this.url = url;
    }

    public UrlBuildResult throwError()
        throws XmlRpcException, MalformedURLException
    {
        if ( error != null )
        {
            if ( error instanceof MalformedURLException )
            {
                throw (MalformedURLException) error;
            }
            else if ( error instanceof XmlRpcException )
            {
                throw (XmlRpcException) error;
            }
        }

        return this;
    }

    public String get()
    {
        return url;
    }
}
