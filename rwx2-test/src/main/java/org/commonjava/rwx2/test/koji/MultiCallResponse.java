package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Response;

import java.util.List;

/**
 * Created by ruhan on 8/4/17.
 */
@Response
public class MultiCallResponse
{
    @DataIndex( 0 )
    @Contains( MultiCallValueObj.class )
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
