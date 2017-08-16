package org.commonjava.rwx.test.koji;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

import java.util.List;

/**
 * Created by ruhan on 8/15/17.
 */
@Response
public class ListBuildResponse
{
    @DataIndex( 0 )
    private List<KojiBuildInfo> builds;

    public ListBuildResponse( List<KojiBuildInfo> builds )
    {
        this.builds = builds;
    }

    public ListBuildResponse()
    {
    }

    public void setBuilds( List<KojiBuildInfo> builds )
    {
        this.builds = builds;
    }

    public List<KojiBuildInfo> getBuilds()
    {
        return builds;
    }
}

