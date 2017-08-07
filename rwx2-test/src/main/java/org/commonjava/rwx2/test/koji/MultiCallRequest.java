package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx2.anno.DataIndex;
import org.commonjava.rwx2.anno.Request;

import java.util.List;

/**
 * Created by ruhan on 8/2/17.
 */
@Request( method = "multiCall" )
public class MultiCallRequest
{
    @DataIndex( 0 )
    private List<MultiCallObj> multiCallObjs;

    public List<MultiCallObj> getMultiCallObjs()
    {
        return multiCallObjs;
    }

    public void setMultiCallObjs( List<MultiCallObj> multiCallObjs )
    {
        this.multiCallObjs = multiCallObjs;
    }
}
