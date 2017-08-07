package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx2.anno.DataIndex;
import org.commonjava.rwx2.anno.Request;

/**
 * Created by ruhan on 7/19/17.
 */
@Request( method="getBuild" )
public class GetBuildByNVRObjRequest
{
    @DataIndex( 0 )
    private KojiNVR nvr;

    public GetBuildByNVRObjRequest() {}

    public GetBuildByNVRObjRequest( KojiNVR nvr )
    {
        this.nvr = nvr;
    }

    public KojiNVR getNvr(){
        return nvr;
    }

    public void setNvr( KojiNVR nvr )
    {
        this.nvr = nvr;
    }

}
