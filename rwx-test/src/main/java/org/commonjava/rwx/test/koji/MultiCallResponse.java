package org.commonjava.rwx.test.koji;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

import java.util.List;

/**
 * Created by ruhan on 8/4/17.
 */
@Response
public class MultiCallResponse
{
    @DataIndex( 0 )
    private List<MultiCallValueObj> valueObjs;

    public List<MultiCallValueObj> getValueObjs()
    {
        return valueObjs;
    }

    public void setValueObjs( List<MultiCallValueObj> valueObjs )
    {
        this.valueObjs = valueObjs;
    }
}
