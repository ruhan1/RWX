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
