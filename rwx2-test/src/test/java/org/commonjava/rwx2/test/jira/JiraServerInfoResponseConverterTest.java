package org.commonjava.rwx2.test.jira;

import org.commonjava.rwx2.api.RWXMapper;
import org.commonjava.rwx2.core.Registry;
import org.commonjava.rwx2.test.AbstractTest;
import org.commonjava.rwx2.test.generated.Test_Registry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 8/7/17.
 */
public class JiraServerInfoResponseConverterTest
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
        String source = getXMLStringIgnoreFormat( "jiraServerInfoResponse" );
        JiraServerInfoResponse response =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), JiraServerInfoResponse.class );
        JiraServerInfo jiraServerInfo = response.getValue();

        assertEquals( "4.1.2", jiraServerInfo.getVersion() );
        assertEquals( "http://jira.codehaus.org", jiraServerInfo.getBaseUrl() );
        assertEquals( "Mon Jun 07 00:00:00 CDT 2010", jiraServerInfo.getBuildDate() );
        assertEquals( 531, jiraServerInfo.getBuildNumber() );
        assertEquals( "Enterprise", jiraServerInfo.getEdition() );
        assertEquals( "com.atlassian.jira.rpc.soap.beans.RemoteTimeInfo@18702b7", jiraServerInfo.getServerTime() );

        String rendered = new RWXMapper().render( response );

        String sortedSource = splitAndSort( formalizeXMLString( source ) );
        String sortedRendered = splitAndSort( formalizeXMLString( rendered ) );

        assertEquals( sortedSource, sortedRendered );
    }

}
