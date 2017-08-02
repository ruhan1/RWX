package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Request;

/**
 * Created by ruhan on 7/19/17.
 */
@Request( method="getBuild" )
public class GetBuildRequest
{
    @DataIndex( 0 )
    private String nvr;

    public GetBuildRequest() {}

    public GetBuildRequest( String nvr )
    {
        this.nvr = nvr;
    }

    public String getNvr(){
        return nvr;
    }

    public void setNvr( String nvr )
    {
        this.nvr = nvr;
    }

}
