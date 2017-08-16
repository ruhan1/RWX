package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.test.AbstractTest;
import org.commonjava.rwx.test.generated.Test_Registry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 8/7/17.
 */
public class JiraServerInfoConverterTest
                extends AbstractTest
{
    @BeforeClass
    public static void register()
    {
        Registry.setInstance( new Test_Registry() );
    }

    @Test
    public void roundTrip_jiraServerInfoResponse() throws Exception
    {
        String source = getXMLString( "jiraServerInfoResponse" );
        JiraServerInfoResponse response = new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ),
                                                                 JiraServerInfoResponse.class );

        assertJiraServerInfo( response.getValue() );

        String rendered = new RWXMapper().render( response );
        JiraServerInfoResponse renderedResponse =
                        new RWXMapper().parse( new ByteArrayInputStream( rendered.getBytes() ),
                                               JiraServerInfoResponse.class );

        assertJiraServerInfo( renderedResponse.getValue() );
    }

    @Test
    public void roundTrip_jiraServerInfoResponseVariantOne() throws Exception
    {
        String source = getXMLString( "jiraServerInfoResponse" );
        JiraServerInfoResponseVariantOne response =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ),
                                               JiraServerInfoResponseVariantOne.class );

        assertJiraServerInfo( response.getValue() );

        String rendered = new RWXMapper().render( response );
        JiraServerInfoResponseVariantOne renderedResponse =
                        new RWXMapper().parse( new ByteArrayInputStream( rendered.getBytes() ),
                                               JiraServerInfoResponseVariantOne.class );

        assertJiraServerInfo( renderedResponse.getValue() );
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
