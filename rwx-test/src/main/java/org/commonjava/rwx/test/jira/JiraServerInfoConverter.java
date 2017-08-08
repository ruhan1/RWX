package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.core.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruhan on 8/7/17.
 */
public class JiraServerInfoConverter implements Converter<JiraServerInfo>
{

    @Override
    public JiraServerInfo parse( Object object )
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
    public Object render( JiraServerInfo value )
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "version", value.getVersion() );
        map.put( "baseUrl", value.getBaseUrl() );
        map.put( "buildDate", value.getBuildDate() );
        map.put( "edition", value.getEdition() );
        map.put( "serverTime", value.getServerTime() );
        map.put( "buildNumber", value.getBuildNumber() );
        return map;
    }
}
