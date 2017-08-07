package org.commonjava.rwx2.test.jira;

import org.commonjava.rwx2.anno.Converter;
import org.commonjava.rwx2.anno.DataIndex;
import org.commonjava.rwx2.anno.Response;

/**
 * Created by ruhan on 8/7/17.
 */
@Response
public class JiraServerInfoResponse
{
    public JiraServerInfo getValue()
    {
        return value;
    }

    public void setValue( JiraServerInfo value )
    {
        this.value = value;
    }

    @Converter( JiraServerInfoConverter.class )
    @DataIndex( 0 )
    private JiraServerInfo value;

}
