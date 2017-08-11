package org.commonjava.rwx.test.simple;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Request;

import java.util.List;

/**
 * Created by ruhan on 8/11/17.
 */
@Request( method="foo" )
public class RequestWithOneArrayParam
{
    @DataIndex( 0 )
    private List<String> array;

    public List<String> getArray()
    {
        return array;
    }

    public void setArray( List<String> array )
    {
        this.array = array;
    }
}
