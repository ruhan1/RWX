package org.commonjava.rwx2.core.simple.generated;

import org.commonjava.rwx2.core.Registry;
import org.commonjava.rwx2.core.simple.RequestWithOneParam;
import org.commonjava.rwx2.core.simple.SimpleRequest;
import org.commonjava.rwx2.core.simple.SimpleResponse;

/**
 * Created by ruhan on 7/19/17.
 */
public class TestRegistry
                extends Registry
{

    public TestRegistry()
    {
        setRenderer( SimpleRequest.class, new SimpleRequest_Renderer() );
        setParser( SimpleResponse.class, new SimpleResponse_Parser() );
        setRenderer( RequestWithOneParam.class, new RequestWithOneParam_Renderer() );
    }

}
