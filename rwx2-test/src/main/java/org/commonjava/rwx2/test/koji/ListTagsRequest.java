package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx2.anno.DataIndex;
import org.commonjava.rwx2.anno.Request;

/**
 * Created by ruhan on 7/19/17.
 */
@Request( method="listTags" )
public class ListTagsRequest
{
    @DataIndex( 0 )
    private String nvr;

    public ListTagsRequest() {}

    public ListTagsRequest( String nvr )
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
