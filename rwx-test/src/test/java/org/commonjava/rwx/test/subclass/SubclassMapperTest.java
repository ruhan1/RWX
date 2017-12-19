package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.test.AbstractTest;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by ruhan on 12/19/17.
 */
public class SubclassMapperTest
                extends AbstractTest
{
    @Test
    public void renderer() throws Exception
    {
        String result = new RWXMapper().render( getInstance() );
    }

    @Test
    public void parser() throws Exception
    {
        InputStream stream = getXMLStream( "responseGetBuildDescription" );
        GetBuildDescriptionResponse response = new RWXMapper().parse( stream, GetBuildDescriptionResponse.class );

        BuildDescription value = response.getValue();
        assertEquals( "foo", value.getName() );

        BuildExtraInfoExtended extraInfo = value.getExtraInfo();
        assertEquals( "user1", extraInfo.getImportInitiator() );
        assertEquals( "linux", extraInfo.getBuildSystem() );

        MavenExtraInfo mavenExtraInfo = extraInfo.getMavenExtraInfo();
        assertEquals( "1.0-SNAPSHOT", mavenExtraInfo.getVersion() );
    }

    private GetBuildDescriptionResponse getInstance()
    {
        GetBuildDescriptionResponse response = new GetBuildDescriptionResponse();
        BuildDescription value = new BuildDescription();
        value.setName( "foo" );
        value.setRelease( "1" );
        value.setVersion( "1.0" );
        BuildExtraInfoExtended extraInfo = new BuildExtraInfoExtended();
        extraInfo.setImportInitiator( "user1" );
        extraInfo.setBuildSystem( "linux" );
        extraInfo.setExternalBuildId( "1001" );
        extraInfo.setExternalBuildUrl( "http://foo/bar" );
        MavenExtraInfo mavenExtraInfo = new MavenExtraInfo();
        mavenExtraInfo.setGroupId( "foo" );
        mavenExtraInfo.setArtifactId( "bar" );
        mavenExtraInfo.setVersion( "1.0-SNAPSHOT" );
        extraInfo.setMavenExtraInfo( mavenExtraInfo );
        value.setExtraInfo( extraInfo );
        response.setValue( value );
        return response;
    }

}

