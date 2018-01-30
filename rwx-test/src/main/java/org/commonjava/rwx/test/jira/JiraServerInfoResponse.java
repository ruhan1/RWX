package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.anno.Converter;
import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

/**
 * Created by ruhan on 8/7/17.
 */
@Response
public class JiraServerInfoResponse
{
    public AbstractJiraServerInfo getValue()
    {
        return value;
    }

    public void setValue( AbstractJiraServerInfo value )
    {
        this.value = value;
    }

    @Converter( JiraServerInfoConverter.class )
    @DataIndex( 0 )
    private AbstractJiraServerInfo value;

}
