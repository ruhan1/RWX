package org.commonjava.rwx.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by jdcasey on 12/3/15.
 */
public interface RequestModifier
{
    void modifyRequest( HttpPost request );
}
