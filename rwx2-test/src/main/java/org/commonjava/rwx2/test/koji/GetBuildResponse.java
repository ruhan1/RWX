package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Response;

/**
 * Created by ruhan on 7/19/17.
 */
@Response
class GetBuildResponse
{
    @DataIndex(0)
    private KojiBuildInfo buildInfo;

}
