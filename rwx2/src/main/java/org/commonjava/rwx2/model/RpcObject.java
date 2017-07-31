package org.commonjava.rwx2.model;

import java.util.List;

/**
 * Created by ruhan on 7/31/17.
 */
public abstract class RpcObject
{
    protected List<Object> params;

    public List<Object> getParams()
    {
        return params;
    }

    public void setParams( List<Object> params )
    {
        this.params = params;
    }
}

