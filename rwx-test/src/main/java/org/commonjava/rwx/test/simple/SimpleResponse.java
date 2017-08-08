package org.commonjava.rwx.test.simple;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

/**
 * Created by ruhan on 7/19/17.
 */
@Response
public class SimpleResponse
{
    public double getValue()
    {
        return value;
    }

    public void setValue( double value )
    {
        this.value = value;
    }

    @DataIndex(0)
    private double value;

}
