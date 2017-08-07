package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx2.anno.DataKey;
import org.commonjava.rwx2.anno.StructPart;

import java.util.List;

/**
 * Created by ruhan on 8/2/17.
 */
@StructPart
public class MultiCallObj
{
    @DataKey( "methodName" )
    private String methodName;

    @DataKey( "params" )
    private List<Object> params;

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName( String methodName )
    {
        this.methodName = methodName;
    }

    public List<Object> getParams()
    {
        return params;
    }

    public void setParams( List<Object> params )
    {
        this.params = params;
    }
}
