package org.commonjava.rwx2.model;

import java.util.List;

/**
 * Responses are much like requests. If the response is successful - the procedure was found,
 * executed correctly, and returned results - then the XML-RPC response will look much like a request,
 * except that the methodCall element is replaced by a methodResponse and there is no methodName.
 *
 * Responses follow some additional constraints:
 *
 * 1. An XML-RPC response can only contain one parameter.
 * 2. That parameter may be an array or a struct, so it is possible to return multiple values.
 * 3. It is always required to return a value in response. A "success value" - perhaps a Boolean set to true (1).
 *
 * Created by ruhan on 7/13/17.
 */
public final class MethodResponse
{
    private List<Object> params;

    public List<Object> getParams()
    {
        return params;
    }

    public void setParams( List<Object> params )
    {
        this.params = params;
    }

}
