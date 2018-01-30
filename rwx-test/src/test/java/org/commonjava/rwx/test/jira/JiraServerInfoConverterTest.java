/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    @Test
    public void roundTrip_jiraServerInfoResponse() throws Exception
    {
        String source = getXMLString( "jiraServerInfoResponse" );
        JiraServerInfoResponse response = new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ),
                                                                 JiraServerInfoResponse.class );

        assertJiraServerInfo( (JiraServerInfo) response.getValue() );

        String rendered = new RWXMapper().render( response );
        JiraServerInfoResponse renderedResponse =
                        new RWXMapper().parse( new ByteArrayInputStream( rendered.getBytes() ),
                                               JiraServerInfoResponse.class );

        assertJiraServerInfo( (JiraServerInfo) renderedResponse.getValue() );
    }

    @Test
    public void roundTrip_jiraServerInfoResponseVariantOne() throws Exception
    {
        String source = getXMLString( "jiraServerInfoResponse" );
        JiraServerInfoResponseVariantOne response =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ),
                                               JiraServerInfoResponseVariantOne.class );

        assertJiraServerInfo( (JiraServerInfo) response.getValue() );

        String rendered = new RWXMapper().render( response );
        JiraServerInfoResponseVariantOne renderedResponse =
                        new RWXMapper().parse( new ByteArrayInputStream( rendered.getBytes() ),
                                               JiraServerInfoResponseVariantOne.class );

        assertJiraServerInfo( (JiraServerInfo) renderedResponse.getValue() );
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
