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
package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.anno.DataKey;
import org.commonjava.rwx.anno.StructPart;

import static org.commonjava.rwx.test.subclass.Constants.BUILD_SYSTEM;
import static org.commonjava.rwx.test.subclass.Constants.EXTERNAL_BUILD_ID;
import static org.commonjava.rwx.test.subclass.Constants.EXTERNAL_BUILD_URL;
import static org.commonjava.rwx.test.subclass.Constants.MAVEN_INFO;

/**
 * Created by ruhan on 12/19/17.
 */
@StructPart
public class BuildExtraInfo
{
    @DataKey( MAVEN_INFO )
    private MavenExtraInfo mavenExtraInfo;

    @DataKey( EXTERNAL_BUILD_ID )
    private String externalBuildId;

    @DataKey( BUILD_SYSTEM )
    private String buildSystem;

    @DataKey( EXTERNAL_BUILD_URL )
    private String externalBuildUrl;

    public MavenExtraInfo getMavenExtraInfo()
    {
        return mavenExtraInfo;
    }

    public void setMavenExtraInfo( MavenExtraInfo mavenExtraInfo )
    {
        this.mavenExtraInfo = mavenExtraInfo;
    }

    public String getExternalBuildId()
    {
        return externalBuildId;
    }

    public void setExternalBuildId( String externalBuildId )
    {
        this.externalBuildId = externalBuildId;
    }

    public String getBuildSystem()
    {
        return buildSystem;
    }

    public void setBuildSystem( String buildSystem )
    {
        this.buildSystem = buildSystem;
    }

    public String getExternalBuildUrl()
    {
        return externalBuildUrl;
    }

    public void setExternalBuildUrl( String externalBuildUrl )
    {
        this.externalBuildUrl = externalBuildUrl;
    }
}
