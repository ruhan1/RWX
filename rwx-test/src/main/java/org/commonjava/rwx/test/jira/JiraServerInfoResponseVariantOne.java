package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

/**
 * Created by ruhan on 8/11/17.
 */
@Response
public class JiraServerInfoResponseVariantOne
{
    public AbstractJiraServerInfo getValue()
    {
        return value;
    }

    public void setValue( AbstractJiraServerInfo value )
    {
        this.value = value;
    }

    @DataIndex( 0 )
    private AbstractJiraServerInfo value; // no @Converter on field, but on class AbstractJiraServerInfo

}
