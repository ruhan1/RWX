package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx2.anno.DataKey;
import org.commonjava.rwx2.anno.StructPart;

import java.util.Map;

/**
 * Created by ruhan on 7/19/17.
 */
@StructPart
public class KojiBuildInfo
{
    @DataKey("build_id")
    private int buildId;

    @DataKey("package_id")
    private int packageId;

    @DataKey("package_name")
    private String packageName;

    @DataKey("version")
    private String version;

    @DataKey("release")
    private String release;

    @DataKey("start_time")
    private String start;

    @DataKey("creation_time")
    private String creation;

    @DataKey("completion_time")
    private String completion;

    @DataKey("id")
    private int id;

    @DataKey("epoch")
    private Object epoch;

    @DataKey("source")
    private Object source;

    @DataKey("state")
    private int state;

    @DataKey("completion_ts")
    private Double completionTs;

    @DataKey("owner_id")
    private int ownerId;

    @DataKey("owner_name")
    private String ownerName;

    @DataKey("nvr")
    private String nvr;

    @DataKey("creation_event_id")
    private int creationEventId;

    @DataKey("start_ts")
    private Double startTs;

    @DataKey("volume_id")
    private int volumeId;

    @DataKey("creation_ts")
    private Double creationTs;

    @DataKey("name")
    private String name;

    @DataKey("task_id")
    private int taskId;

    @DataKey("volume_name")
    private String volumeName;

    @DataKey("extra")
    private Map<String, Object> extra;

    public KojiBuildInfo() {}

    //@KeyRefs({"build_id", "package_id", "package_name", "version", "release"})
    public KojiBuildInfo(int id, int packageId, String name, String version, String release)
    {
        setId( id );
        setPackageId( packageId );
        setName( name );
        setVersion( version );
        setRelease( release );
    }

    public int getBuildId()
    {
        return buildId;
    }

    public void setBuildId( int buildId )
    {
        this.buildId = buildId;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName( String packageName )
    {
        this.packageName = packageName;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart( String start )
    {
        this.start = start;
    }

    public String getCreation()
    {
        return creation;
    }

    public void setCreation( String creation )
    {
        this.creation = creation;
    }

    public String getCompletion()
    {
        return completion;
    }

    public void setCompletion( String completion )
    {
        this.completion = completion;
    }

    public Object getEpoch()
    {
        return epoch;
    }

    public void setEpoch( Object epoch )
    {
        this.epoch = epoch;
    }

    public Object getSource()
    {
        return source;
    }

    public void setSource( Object source )
    {
        this.source = source;
    }

    public int getState()
    {
        return state;
    }

    public void setState( int state )
    {
        this.state = state;
    }

    public Double getCompletionTs()
    {
        return completionTs;
    }

    public void setCompletionTs( Double completionTs )
    {
        this.completionTs = completionTs;
    }

    public int getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId( int ownerId )
    {
        this.ownerId = ownerId;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName( String ownerName )
    {
        this.ownerName = ownerName;
    }

    public String getNvr()
    {
        return nvr;
    }

    public void setNvr( String nvr )
    {
        this.nvr = nvr;
    }

    public int getCreationEventId()
    {
        return creationEventId;
    }

    public void setCreationEventId( int creationEventId )
    {
        this.creationEventId = creationEventId;
    }

    public Double getStartTs()
    {
        return startTs;
    }

    public void setStartTs( Double startTs )
    {
        this.startTs = startTs;
    }

    public int getVolumeId()
    {
        return volumeId;
    }

    public void setVolumeId( int volumeId )
    {
        this.volumeId = volumeId;
    }

    public Double getCreationTs()
    {
        return creationTs;
    }

    public void setCreationTs( Double creationTs )
    {
        this.creationTs = creationTs;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId( int taskId )
    {
        this.taskId = taskId;
    }

    public String getVolumeName()
    {
        return volumeName;
    }

    public void setVolumeName( String volumeName )
    {
        this.volumeName = volumeName;
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
