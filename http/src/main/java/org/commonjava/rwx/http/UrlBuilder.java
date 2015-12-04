package org.commonjava.rwx.http;

import java.net.MalformedURLException;

/**
 * Created by jdcasey on 12/3/15.
 */
public interface UrlBuilder
{
    String buildUrl( String baseUrl ) throws MalformedURLException;
}
