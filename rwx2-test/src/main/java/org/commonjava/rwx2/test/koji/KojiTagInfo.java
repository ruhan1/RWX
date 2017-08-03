package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.binding.anno.SkipNull;
import org.commonjava.rwx.binding.anno.StructPart;

import java.util.List;

/**
 * Created by ruhan on 1/8/17.
 */
@StructPart
public class KojiTagInfo
{
    @DataKey( "id" )
    @SkipNull
    private Integer id;

    @DataKey( "name" )
    private String name;

    @DataKey( "perm" )
    @SkipNull
    private String permission;

    @DataKey( "perm_id" )
    @SkipNull
    private Integer permissionId;

    @DataKey( "arches" )
    private List<String> arches;

    @DataKey( "locked" )
    @SkipNull
    private Boolean locked;

    @DataKey( "maven_support" )
    private boolean mavenSupport = true;

    @DataKey( "maven_include_all" )
    private boolean mavenIncludeAll = true;

    public KojiTagInfo(){}

    public KojiTagInfo( String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPermission()
    {
        return permission;
    }

    public int getPermissionId()
    {
        return permissionId;
    }

    public List<String> getArches()
    {
        return arches;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public boolean getMavenSupport()
    {
        return mavenSupport;
    }

    public boolean getMavenIncludeAll()
    {
        return mavenIncludeAll;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setPermission( String permission )
    {
        this.permission = permission;
    }

    public void setPermissionId( int permissionId )
    {
        this.permissionId = permissionId;
    }

    public void setArches( List<String> arches )
    {
        this.arches = arches;
    }

    public void setLocked( boolean locked )
    {
        this.locked = locked;
    }

    public void setMavenSupport( boolean mavenSupport )
    {
        this.mavenSupport = mavenSupport;
    }

    public void setMavenIncludeAll( boolean mavenIncludeAll )
    {
        this.mavenIncludeAll = mavenIncludeAll;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public void setPermissionId( Integer permissionId )
    {
        this.permissionId = permissionId;
    }

    public Boolean getLocked()
    {
        return locked;
    }

    public void setLocked( Boolean locked )
    {
        this.locked = locked;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof KojiTagInfo ) )
        {
            return false;
        }

        KojiTagInfo that = (KojiTagInfo) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode()
    {
        return Integer.valueOf( getId() ).hashCode();
    }
}
