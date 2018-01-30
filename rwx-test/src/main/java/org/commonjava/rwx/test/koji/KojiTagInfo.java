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
package org.commonjava.rwx.test.koji;

import org.commonjava.rwx.anno.DataKey;
import org.commonjava.rwx.anno.StructPart;

import java.util.List;

/**
 * Created by ruhan on 1/8/17.
 */
@StructPart
public class KojiTagInfo
{
    @DataKey( "id" )
    private Integer id;

    @DataKey( "name" )
    private String name;

    @DataKey( "perm" )
    private String permission;

    @DataKey( "perm_id" )
    private Integer permissionId;

    @DataKey( "arches" )
    private List<String> arches;

    @DataKey( "locked" )
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

    public Integer getPermissionId()
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
