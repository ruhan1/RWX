/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.commonjava.rwx.apps.jira;

import org.commonjava.rwx.binding.anno.KeyRefs;
import org.commonjava.rwx.binding.anno.StructPart;

@StructPart
public class ServerInfo
{

    private final String version;

    private String baseUrl;

    private String buildDate;

    private int buildNumber;

    private String edition;

    @KeyRefs( "version" )
    public ServerInfo( final String version )
    {
        this.version = version;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl( final String baseUrl )
    {
        this.baseUrl = baseUrl;
    }

    public String getBuildDate()
    {
        return buildDate;
    }

    public void setBuildDate( final String buildDate )
    {
        this.buildDate = buildDate;
    }

    public int getBuildNumber()
    {
        return buildNumber;
    }

    public void setBuildNumber( final int buildNumber )
    {
        this.buildNumber = buildNumber;
    }

    public String getEdition()
    {
        return edition;
    }

    public void setEdition( final String edition )
    {
        this.edition = edition;
    }

    public String getVersion()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return "ServerInfo [baseUrl=" + baseUrl + ", buildDate=" + buildDate + ", buildNumber=" + buildNumber
            + ", edition=" + edition + ", version=" + version + "]";
    }

}
