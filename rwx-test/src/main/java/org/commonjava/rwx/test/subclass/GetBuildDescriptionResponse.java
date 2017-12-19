package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

/**
 * Created by ruhan on 12/19/17.
 */
@Response
public class GetBuildDescriptionResponse
{
    @DataIndex(0)
    private BuildDescription value;

    public BuildDescription getValue()
    {
        return value;
    }

    public void setValue( BuildDescription value )
    {
        this.value = value;
    }
}
