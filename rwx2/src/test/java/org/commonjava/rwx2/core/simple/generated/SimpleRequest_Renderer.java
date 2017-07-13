package org.commonjava.rwx2.core.simple.generated;

import org.commonjava.rwx2.core.Renderer;
import org.commonjava.rwx2.core.simple.SimpleRequest;
import org.commonjava.rwx2.model.MethodCall;

/**
 * Created by ruhan on 7/19/17.
 */
public class SimpleRequest_Renderer implements Renderer<SimpleRequest>
{
    @Override
    public MethodCall render( SimpleRequest value )
    {
        MethodCall ret = new MethodCall();
        ret.setMethodName( "foo" );
        return ret;
    }
}
