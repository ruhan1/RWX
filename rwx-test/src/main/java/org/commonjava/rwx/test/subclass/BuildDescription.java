package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.anno.DataKey;
import org.commonjava.rwx.anno.StructPart;

import static org.commonjava.rwx.test.subclass.Constants.EXTRA_INFO;
import static org.commonjava.rwx.test.subclass.Constants.NAME;
import static org.commonjava.rwx.test.subclass.Constants.RELEASE;
import static org.commonjava.rwx.test.subclass.Constants.VERSION;

/**
 * Created by ruhan on 12/19/17.
 */
@StructPart
public class BuildDescription
{
    @DataKey( NAME )
    private String name;

    @DataKey( VERSION )
    private String version;

    @DataKey( RELEASE )
    private String release;

    @DataKey( EXTRA_INFO )
    private BuildExtraInfoExtended extraInfo;

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

    public BuildExtraInfoExtended getExtraInfo()
    {
        return extraInfo;
    }

    public void setExtraInfo( BuildExtraInfoExtended extraInfo )
    {
        this.extraInfo = extraInfo;
    }
}
