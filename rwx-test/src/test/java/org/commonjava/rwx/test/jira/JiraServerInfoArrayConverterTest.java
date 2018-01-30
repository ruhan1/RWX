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
