package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

import java.util.List;

/**
 * Created by ruhan on 8/11/17.
 */
@Response
public class JiraServerInfoArrayResponse
{
    public List<AbstractJiraServerInfo> getValues()
    {
        return values;
    }

    public void setValues( List<AbstractJiraServerInfo> values )
    {
        this.values = values;
    }

    @DataIndex( 0 )
    private List<AbstractJiraServerInfo> values; // List of objects, @Converter on super class AbstractJiraServerInfo

}
