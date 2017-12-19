package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.anno.DataKey;
import org.commonjava.rwx.anno.StructPart;

import static org.commonjava.rwx.test.subclass.Constants.ARTIFACT_ID;
import static org.commonjava.rwx.test.subclass.Constants.GROUP_ID;
import static org.commonjava.rwx.test.subclass.Constants.VERSION;

/**
 * Created by ruhan on 12/19/17.
 */
@StructPart
public class MavenExtraInfo
{
    @DataKey( GROUP_ID )
    private String groupId;

    @DataKey( ARTIFACT_ID )
    private String artifactId;

    @DataKey( VERSION )
    private String version;

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }
}
