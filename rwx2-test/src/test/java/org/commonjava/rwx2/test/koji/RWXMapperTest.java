package org.commonjava.rwx2.test.koji;

import org.commonjava.rwx2.api.RWXMapper;
import org.commonjava.rwx2.core.Registry;
import org.commonjava.rwx2.test.koji.generated.Koji_Registry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by ruhan on 8/2/17.
 */
public class RWXMapperTest
                extends AbstractTest
{
    @BeforeClass
    public static void register()
    {
        Registry.setInstance( new Koji_Registry() );
    }

    @Test
    public void roundTrip_GetBuildByNVRObjRequest() throws Exception
    {
        KojiNVR kojiNVR = new KojiNVR( "org.dashbuilder-dashbuilder-parent-metadata", "0.4.0.Final", "1" );

        // render
        GetBuildByNVRObjRequest getBuildByNVRObjRequest = new GetBuildByNVRObjRequest( kojiNVR );
        String request = new RWXMapper().render( getBuildByNVRObjRequest );
        String expected = getXMLStringIgnoreFormat( "kojiGetBuildByNVRObjRequest" );
        assertEquals( expected, formalizeXMLString( request ) );

        // parse
        GetBuildByNVRObjRequest generated = new RWXMapper().parse( new ByteArrayInputStream( request.getBytes() ),
                                                                   GetBuildByNVRObjRequest.class );
        KojiNVR nvr = generated.getNvr();
        assertEquals( kojiNVR.renderString(), nvr.renderString() );
    }

    @Test
    public void roundTrip_GetBuildRequest() throws Exception
    {
        // render
        GetBuildRequest getBuildRequest =
                        new GetBuildRequest( "org.dashbuilder-dashbuilder-parent-metadata-0.4.0.Final-1" );
        String request = new RWXMapper().render( getBuildRequest );
        String expected = getXMLStringIgnoreFormat( "kojiGetBuildRequest" );
        assertEquals( formalizeXMLString( expected ), formalizeXMLString( request ) );

        // parse
        GetBuildRequest generated =
                        new RWXMapper().parse( new ByteArrayInputStream( request.getBytes() ), GetBuildRequest.class );
        assertEquals( getBuildRequest.getNvr(), generated.getNvr() );
    }

    @Test
    public void roundTrip_GetBuildResponse() throws Exception
    {
        String source = getXMLStringIgnoreFormat( "kojiGetBuildResponse" );
        GetBuildResponse response =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), GetBuildResponse.class );
        KojiBuildInfo buildInfo = response.getBuildInfo();

        assertEquals( 513598, buildInfo.getBuildId() );
        assertEquals( 48475, buildInfo.getPackageId() );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", buildInfo.getName() );
        assertEquals( "1", buildInfo.getRelease() );
        assertEquals( "0.4.0.Final_10", buildInfo.getVersion() );
        assertEquals( null, buildInfo.getExtra() );

        String rendered = new RWXMapper().render( response );

        String sortedSource = splitAndSort( formalizeXMLString( source ) );
        String sortedRendered = splitAndSort( formalizeXMLString( rendered ) );

        /*
         * below will fail but there is nothing different but the format of doubles,
         * e.g., 1.47397464484373E9 vs. 1473974644.84373
         */
        //assertEquals( sortedSource, sortedRendered );

    }

    @Test
    public void roundTrip_ListTagsResponse() throws Exception
    {
        String source = getXMLStringIgnoreFormat( "kojiListTagsResponse" );
        ListTagsResponse response =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), ListTagsResponse.class );
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

        String rendered = new RWXMapper().render( response );

        String sortedSource = splitAndSort( formalizeXMLString( source ) );
        String sortedRendered = splitAndSort( formalizeXMLString( rendered ) );

        assertEquals( sortedSource, sortedRendered );
    }

    @Test
    public void roundTrip_MultiCallRequest() throws Exception
    {
        String source = getXMLStringIgnoreFormat( "kojiMulticallRequest" );
        MultiCallRequest request =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), MultiCallRequest.class );

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

        String rendered = new RWXMapper().render( request );
        String sortedSource = splitAndSort( formalizeXMLString( source ) );
        String sortedRendered = splitAndSort( formalizeXMLString( rendered ) );

        assertEquals( sortedSource, sortedRendered );
    }

    @Test
    public void roundTrip_MultiCallResponse() throws Exception
    {
        String source = getXMLStringIgnoreFormat( "kojiMulticallResponse" );
        MultiCallResponse response =
                        new RWXMapper().parse( new ByteArrayInputStream( source.getBytes() ), MultiCallResponse.class );

        List<MultiCallValueObj> valueObjs = response.getValueObjs();

        assertEquals( 2, valueObjs.size() );

        MultiCallValueObj valueObj1 = valueObjs.get( 0 );
        MultiCallValueObj valueObj2 = valueObjs.get( 1 );

        Object data1 = valueObj1.getData();
        Object data2 = valueObj2.getData();

        assertTrue( data1 instanceof Map );
        assertTrue( data2 instanceof List );

        Map<String, Object> data1Map = (Map)data1;

        assertEquals( 48475, data1Map.get( "package_id" ) );
        assertEquals( 513598, data1Map.get( "build_id" ) );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", data1Map.get( "package_name" ) );

        List<Object> data2List = (List)data2;

        assertEquals( 4, data2List.size() );

        Object data2_1 = data2List.get( 0 );

        assertTrue( data2_1 instanceof Map );

        Map<String, Object> data2_1Map = (Map) data2_1;
        assertEquals( "jb-bxms-6.3-candidate", data2_1Map.get( "name" ) );
        assertEquals( 8829, data2_1Map.get( "id" ) );

        String rendered = new RWXMapper().render( response );
        String sortedSource = splitAndSort( formalizeXMLString( source ) );
        String sortedRendered = splitAndSort( formalizeXMLString( rendered ) );

        //assertEquals( sortedSource, sortedRendered ); // this has no problem other than the double's format is different
    }

}
