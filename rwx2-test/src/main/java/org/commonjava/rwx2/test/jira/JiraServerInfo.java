package org.commonjava.rwx2.test.jira;

/**
 * Created by ruhan on 8/7/17.
 */
public final class JiraServerInfo
{
    private String version;
    private String baseUrl;
    private String buildDate;
    private int buildNumber;
    private String edition;
    private String serverTime;

    public JiraServerInfo( String version, String baseUrl, String buildDate, int buildNumber )
    {
        this.version = version;
        this.baseUrl = baseUrl;
        this.buildDate = buildDate;
        this.buildNumber = buildNumber;
    }

    public String getVersion()
    {
        return version;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public String getBuildDate()
    {
        return buildDate;
    }

    public int getBuildNumber()
    {
        return buildNumber;
    }

    public String getEdition()
    {
        return edition;
    }

    public String getServerTime()
    {
        return serverTime;
    }

    public void setEdition( String edition )
    {
        this.edition = edition;
    }

    public void setServerTime( String serverTime )
    {
        this.serverTime = serverTime;
    }
}
