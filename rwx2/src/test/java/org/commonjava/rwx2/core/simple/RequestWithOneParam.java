package org.commonjava.rwx2.core.simple;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Request;

/**
 * Created by ruhan on 7/19/17.
 */
@Request( method="foo" )
public class RequestWithOneParam
{
    @DataIndex( 0 )
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }
}
