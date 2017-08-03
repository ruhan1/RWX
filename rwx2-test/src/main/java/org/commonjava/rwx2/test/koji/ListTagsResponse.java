package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Response;

import java.util.List;

/**
 * Created by ruhan on 7/19/17.
 */
@Response
public class ListTagsResponse
{
    @DataIndex( 0 )
    @Contains( KojiTagInfo.class )
    private List<KojiTagInfo> tags;

    public List<KojiTagInfo> getTags()
    {
        return tags;
    }

    public void setTags( List<KojiTagInfo> tags )
    {
        this.tags = tags;
    }
}