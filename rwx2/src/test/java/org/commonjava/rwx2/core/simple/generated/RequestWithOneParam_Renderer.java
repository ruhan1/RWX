package org.commonjava.rwx2.core.simple.generated;

import org.commonjava.rwx2.core.Renderer;
import org.commonjava.rwx2.core.simple.RequestWithOneParam;
import org.commonjava.rwx2.model.MethodCall;
import org.commonjava.rwx2.model.RpcObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruhan on 7/19/17.
 */
public class RequestWithOneParam_Renderer
                implements Renderer<RequestWithOneParam>
{
    @Override
    public RpcObject render( RequestWithOneParam request )
    {
        MethodCall ret = new MethodCall();
        ret.setMethodName( "foo" );

        List<Object> params = new ArrayList<>();
        params.add( request.getValue() );
        ret.setParams( params );

        return ret;
    }
}
