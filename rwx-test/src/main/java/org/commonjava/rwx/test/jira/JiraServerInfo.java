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

/**
 * Created by ruhan on 8/7/17.
 */
public final class JiraServerInfo extends AbstractJiraServerInfo
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
