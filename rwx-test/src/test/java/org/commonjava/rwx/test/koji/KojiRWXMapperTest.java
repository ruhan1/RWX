package org.commonjava.rwx.test.koji;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.test.AbstractTest;
import org.commonjava.rwx.test.generated.Test_Registry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by ruhan on 8/2/17.
 */
public class KojiRWXMapperTest
                extends AbstractTest
{
    @BeforeClass
    public static void register()
    {
        Registry.setInstance( new Test_Registry() );
    }

    @Test
    public void roundTrip_GetBuildByNVRObjRequest() throws Exception
    {
        String source = getXMLString( "kojiGetBuildByNVRObjRequest" );
        GetBuildByNVRObjRequest parsed = new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ),
                                                                GetBuildByNVRObjRequest.class );

        GetBuildByNVRObjRequest expected = new GetBuildByNVRObjRequest(
                        new KojiNVR( "org.dashbuilder-dashbuilder-parent-metadata", "0.4.0.Final", "1" ) );

        assertEquals( expected.getNvr().renderString(), parsed.getNvr().renderString() );

        GetBuildByNVRObjRequest rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( parsed ).getBytes() ),
                                               GetBuildByNVRObjRequest.class );
        assertEquals( expected.getNvr().renderString(), rounded.getNvr().renderString() );
    }

    @Test
    public void roundTrip_GetBuildRequest() throws Exception
    {
        String source = getXMLString( "kojiGetBuildRequest" );
        GetBuildRequest parsed =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), GetBuildRequest.class );

        GetBuildRequest expected = new GetBuildRequest( "org.dashbuilder-dashbuilder-parent-metadata-0.4.0.Final-1" );

        assertEquals( expected.getNvr(), parsed.getNvr() );

        GetBuildRequest rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( parsed ).getBytes() ),
                                               GetBuildRequest.class );

        assertEquals( expected.getNvr(), rounded.getNvr() );
    }

    @Test
    public void roundTrip_GetBuildResponse() throws Exception
    {
        String source = getXMLString( "kojiGetBuildResponse" );

        GetBuildResponse parsed =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), GetBuildResponse.class );

        assertGetBuildResponse( parsed );

        GetBuildResponse rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( parsed ).getBytes() ),
                                               GetBuildResponse.class );

        assertGetBuildResponse( rounded );
    }

    private void assertGetBuildResponse( GetBuildResponse response )
    {
        KojiBuildInfo buildInfo = response.getBuildInfo();

        assertEquals( 513598, buildInfo.getBuildId() );
        assertEquals( 48475, buildInfo.getPackageId() );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", buildInfo.getName() );
        assertEquals( "1", buildInfo.getRelease() );
        assertEquals( "0.4.0.Final_10", buildInfo.getVersion() );
        assertEquals( null, buildInfo.getExtra() );
    }

    @Test
    public void roundTrip_ListBuildResponseNIL() throws Exception
    {
        String source = getXMLStringIgnoreFormat( "kojiListBuildsResponseNIL" );
        ListBuildResponse parsed =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), ListBuildResponse.class );

        assertEquals( null, parsed.getBuilds() );

        String rendered = new RWXMapper().render( parsed );

        assertEquals( formalizeXMLString( source ), formalizeXMLString( rendered ) );
    }

    @Test
    public void roundTrip_ListTagsResponse() throws Exception
    {
        String source = getXMLString( "kojiListTagsResponse" );
        ListTagsResponse parsed =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), ListTagsResponse.class );

        assertListTagsResponse( parsed );

        ListTagsResponse rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( parsed ).getBytes() ),
                                               ListTagsResponse.class );

        assertListTagsResponse( rounded );
    }

    private void assertListTagsResponse( ListTagsResponse response )
    {
        List<KojiTagInfo> tags = response.getTags();

        assertEquals( 4, tags.size() );

        for ( KojiTagInfo tag : tags )
        {
            assertEquals( Boolean.FALSE, tag.getLocked() );
            assertEquals( true, tag.getMavenSupport() );
            assertEquals( true, tag.getMavenIncludeAll() );
            assertEquals( null, tag.getPermission() );
            assertEquals( null, tag.getPermissionId() );
            assertEquals( null, tag.getArches() );
        }

        KojiTagInfo tag0 = tags.get( 0 );
        KojiTagInfo tag1 = tags.get( 1 );
        KojiTagInfo tag2 = tags.get( 2 );
        KojiTagInfo tag3 = tags.get( 3 );

        assertEquals( 8829, tag0.getId() );
        assertEquals( 8913, tag1.getId() );
        assertEquals( 9761, tag2.getId() );
        assertEquals( 10559, tag3.getId() );

        assertEquals( "jb-bxms-6.3-candidate", tag0.getName() );
        assertEquals( "jb-cs-maven-candidate", tag1.getName() );
        assertEquals( "jb-fis-2.0-maven-imports", tag2.getName() );
        assertEquals( "jb-mm-7.0-maven-candidate", tag3.getName() );
    }

    @Test
    public void roundTrip_MultiCallRequest() throws Exception
    {
        String source = getXMLString( "kojiMulticallRequest" );
        MultiCallRequest parsed =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), MultiCallRequest.class );

        assertMultiCallRequest( parsed );

        MultiCallRequest rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( parsed ).getBytes() ),
                                               MultiCallRequest.class );

        assertMultiCallRequest( rounded );
    }

    private void assertMultiCallRequest( MultiCallRequest request )
    {
        List<MultiCallObj> multiObjs = request.getMultiCallObjs();

        assertEquals( 2, multiObjs.size() );

        MultiCallObj mObj1 = multiObjs.get( 0 );
        MultiCallObj mObj2 = multiObjs.get( 1 );

        assertEquals( "getBuild", mObj1.getMethodName() );
        assertEquals( "listTags", mObj2.getMethodName() );

        List<Object> mParams1 = mObj1.getParams();
        List<Object> mParams2 = mObj2.getParams();

        assertEquals( 1, mParams1.size() );
        assertEquals( 1, mParams2.size() );

        String nvr = "org.dashbuilder-dashbuilder-parent-metadata-0.4.0.Final-1";
        assertEquals( nvr, mParams1.get( 0 ) );
        assertEquals( nvr, mParams2.get( 0 ) );
    }

    @Test
    public void roundTrip_MultiCallResponse() throws Exception
    {
        String source = getXMLString( "kojiMulticallResponse" );
        MultiCallResponse parsed =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), MultiCallResponse.class );

        assertMultiCallResponse( parsed );

        MultiCallResponse rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( parsed ).getBytes() ),
                                               MultiCallResponse.class );

        assertMultiCallResponse( rounded );
    }

    private void assertMultiCallResponse( MultiCallResponse response )
    {
        List<MultiCallValueObj> valueObjs = response.getValueObjs();

        assertEquals( 2, valueObjs.size() );

        MultiCallValueObj valueObj1 = valueObjs.get( 0 );
        MultiCallValueObj valueObj2 = valueObjs.get( 1 );

        Object data1 = valueObj1.getData();
        Object data2 = valueObj2.getData();

        assertTrue( data1 instanceof Map );
        assertTrue( data2 instanceof List );

        Map<String, Object> data1Map = (Map) data1;

        assertEquals( 48475, data1Map.get( "package_id" ) );
        assertEquals( 513598, data1Map.get( "build_id" ) );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", data1Map.get( "package_name" ) );

        List<Object> data2List = (List) data2;

        assertEquals( 4, data2List.size() );

        Object data2_1 = data2List.get( 0 );

        assertTrue( data2_1 instanceof Map );

        Map<String, Object> data2_1Map = (Map) data2_1;
        assertEquals( "jb-bxms-6.3-candidate", data2_1Map.get( "name" ) );
        assertEquals( 8829, data2_1Map.get( "id" ) );
    }

}
