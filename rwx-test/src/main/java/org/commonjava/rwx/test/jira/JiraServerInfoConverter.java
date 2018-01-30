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

import org.commonjava.rwx.core.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruhan on 8/7/17.
 */
public class JiraServerInfoConverter implements Converter<AbstractJiraServerInfo>
{

    @Override
    public AbstractJiraServerInfo parse( Object object )
    {
        Map<String, Object> map = (Map) object;
        String version = (String) map.get( "version" );
        String baseUrl = (String) map.get( "baseUrl" );
        String buildDate = (String) map.get( "buildDate" );
        String edition = (String) map.get( "edition" );
        String serverTime = (String) map.get( "serverTime" );
        int buildNumber = (Integer) map.get( "buildNumber" );

        JiraServerInfo ret = new JiraServerInfo(version, baseUrl, buildDate, buildNumber);
        ret.setEdition(edition);
        ret.setServerTime(serverTime);
        return ret;
    }

    @Override
    public Object render( AbstractJiraServerInfo value )
    {
        Map<String, Object> map = new HashMap<>();
        if ( value instanceof JiraServerInfo )
        {
            JiraServerInfo jiraServerInfo = (JiraServerInfo) value;
            map.put( "version", jiraServerInfo.getVersion() );
            map.put( "baseUrl", jiraServerInfo.getBaseUrl() );
            map.put( "buildDate", jiraServerInfo.getBuildDate() );
            map.put( "edition", jiraServerInfo.getEdition() );
            map.put( "serverTime", jiraServerInfo.getServerTime() );
            map.put( "buildNumber", jiraServerInfo.getBuildNumber() );
        }
        return map;
    }
}
