package org.commonjava.rwx2.core.koji;

import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.binding.anno.KeyRefs;
import org.commonjava.rwx.binding.anno.StructPart;

import java.util.Map;

/**
 * Created by ruhan on 7/19/17.
 */
@StructPart
class KojiBuildInfo
{
    @DataKey("build_id")
    private int id;
    @DataKey("package_id")
    private int packageId;
    @DataKey("package_name")
    private String name;
    @DataKey("version")
    private String version;
    @DataKey("release")
    private String release;
    // ...
    @DataKey("extra")
    private Map<String, Object> extra;

    public KojiBuildInfo() {}

    @KeyRefs({"build_id", "package_id", "package_name", "version", "release"})
    public KojiBuildInfo(int id, int packageId, String name, String version, String release)
    {
        setId( id );
        setPackageId( packageId );
        setName( name );
        setVersion( version );
        setRelease( release );
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getPackageId()
    {
        return packageId;
    }

    public void setPackageId( int packageId )
    {
        this.packageId = packageId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getRelease()
    {
        return release;
    }

    public void setRelease( String release )
    {
        this.release = release;
    }

    public Map<String, Object> getExtra()
    {
        return extra;
    }

    public void setExtra( Map<String, Object> extra )
    {
        this.extra = extra;
    }
}
