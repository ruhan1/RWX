package org.commonjava.rwx2.core.koji;

import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.binding.anno.KeyRefs;
import org.commonjava.rwx.binding.anno.StructPart;

/**
 * Created by ruhan on 7/19/17.
 */
@StructPart
public class KojiNVR
{
    @DataKey( "packageID" )
    private String packageId;

    @DataKey( "userID" )
    private String userId;

    @DataKey( "tagID" )
    private String tagId;

    @DataKey( "name" )
    private String name;

    @DataKey( "version" )
    private String version;

    @DataKey( "release" )
    private String release;

    @KeyRefs( {"name", "version", "release"} )
    public KojiNVR( String name, String version, String release )
    {
        this.name = name;
        this.version = version;
        this.release = release;
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public String getRelease()
    {
        return release;
    }

    public String renderString()
    {
        return String.format( "%s-%s-%s", getName(), getVersion().replace( '-', '_' ), getRelease() );
    }
}
