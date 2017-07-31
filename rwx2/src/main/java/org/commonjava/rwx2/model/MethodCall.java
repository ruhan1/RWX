package org.commonjava.rwx2.model;

import java.util.Collections;
import java.util.List;

/**
 * Each request contains a single XML document, whose root element is a methodCall element.
 * Each methodCall element contains a methodName element and a params element. The methodName element identifies
 * the name of the procedure to be called, while the params element contains a list of parameters and their values.
 *
 * Created by ruhan on 7/13/17.
 */
public final class MethodCall
                extends RpcObject
{
    private String methodName;

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName( String methodName )
    {
        this.methodName = methodName;
    }
}
