package org.commonjava.rwx2.core.simple.generated;

import org.commonjava.rwx2.core.Parser;
import org.commonjava.rwx2.core.simple.SimpleResponse;
import org.commonjava.rwx2.model.MethodResponse;
import org.commonjava.rwx2.model.RpcObject;

import java.util.List;

/**
 * Created by ruhan on 7/19/17.
 */
public class SimpleResponse_Parser
                implements Parser<SimpleResponse>
{
    @Override
    public SimpleResponse parse( Object object )
    {
        SimpleResponse ret = new SimpleResponse();

        List<Object> params = ((RpcObject)object).getParams();
        ret.setValue( (Double) params.get( 0 ) );

        return ret;
    }
}
