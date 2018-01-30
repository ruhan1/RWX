package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.test.AbstractTest;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 8/7/17.
 */
public class JiraServerInfoArrayConverterTest
                extends AbstractTest
{
    @Test
    public void roundTrip() throws Exception
    {
        String source = getXMLString( "jiraServerInfoArrayResponse" );
        JiraServerInfoArrayResponse response = new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ),
                                                                 JiraServerInfoArrayResponse.class );

        String rendered = new RWXMapper().render( response );
        JiraServerInfoArrayResponse renderedResponse =
                        new RWXMapper().parse( new ByteArrayInputStream( rendered.getBytes() ),
                                               JiraServerInfoArrayResponse.class );

        assertJiraServerInfo( (JiraServerInfo) renderedResponse.getValues().get( 0 ) );
    }

    private void assertJiraServerInfo( JiraServerInfo jiraServerInfo )
    {
        assertEquals( "4.1.2", jiraServerInfo.getVersion() );
        assertEquals( "http://jira.codehaus.org", jiraServerInfo.getBaseUrl() );
        assertEquals( "Mon Jun 07 00:00:00 CDT 2010", jiraServerInfo.getBuildDate() );
        assertEquals( 531, jiraServerInfo.getBuildNumber() );
        assertEquals( "Enterprise", jiraServerInfo.getEdition() );
        assertEquals( "com.atlassian.jira.rpc.soap.beans.RemoteTimeInfo@18702b7", jiraServerInfo.getServerTime() );
    }
}
