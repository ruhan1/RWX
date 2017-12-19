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
