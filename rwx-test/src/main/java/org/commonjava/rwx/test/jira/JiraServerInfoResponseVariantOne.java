package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

/**
 * Created by ruhan on 8/11/17.
 */
@Response
public class JiraServerInfoResponseVariantOne
{
    public JiraServerInfo getValue()
    {
        return value;
    }

    public void setValue( JiraServerInfo value )
    {
        this.value = value;
    }

    @DataIndex( 0 )
    private JiraServerInfo value; // no @Converter on field, but on class AbstractJiraServerInfo

}
