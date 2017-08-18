package org.commonjava.rwx.test.koji;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.core.Parser;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.core.Renderer;
import org.commonjava.rwx.test.AbstractTest;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by ruhan on 8/2/17.
 */
public class KojiMultiCallTest
                extends AbstractTest
{
    @Test
    public void multiCallRequest_renderTest_0() throws Exception
    {
        MultiCallRequest request = new MultiCallRequest();
        List<MultiCallObj> multiObjs = new ArrayList<>(  );

        MultiCallObj multiCallObj = new MultiCallObj();
        multiCallObj.setMethodName( "getBuild" );

        KojiNVR kojiNVR = new KojiNVR();
        kojiNVR.setName( "test" );
        kojiNVR.setVersion( "1.0-Final" );
        kojiNVR.setRelease( "1" );

        Renderer<KojiNVR> renderer_KojiNVR = Registry.getInstance().getRenderer( KojiNVR.class );
        Object renderedKojiNVR = renderer_KojiNVR.render( kojiNVR );

        List params = new ArrayList<>(  );
        params.add( renderedKojiNVR );
        multiCallObj.setParams( params );

        multiObjs.add( multiCallObj );

        request.setMultiCallObjs( multiObjs );

        assertEquals( getXMLStringIgnoreFormat( "kojiMulticallRequest-0" ), new RWXMapper().render( request ) );
    }

    @Test
    public void multiCallRequest_renderTest() throws Exception
    {
        MultiCallRequest request = new MultiCallRequest();
        List<MultiCallObj> multiObjs = new ArrayList<>(  );

        MultiCallObj callObj_1 = new MultiCallObj( "getBuild" );
        List params = new ArrayList<>(  );
        params.add( "org.dashbuilder-dashbuilder-parent-metadata-0.4.0.Final-1" );
        callObj_1.setParams( params );
        multiObjs.add( callObj_1 );

        MultiCallObj callObj_2 = new MultiCallObj( "listTags" );
        params = new ArrayList<>(  );
        params.add( "org.jbpm-jbpm-parent-metadata-0.4.0.Final-1" );
        callObj_2.setParams( params );
        multiObjs.add( callObj_2 );

        request.setMultiCallObjs( multiObjs );

        MultiCallRequest rounded =
                        new RWXMapper().parse( new ByteArrayInputStream( new RWXMapper().render( request ).getBytes() ),
                                               MultiCallRequest.class );

        assertMultiCallRequest( rounded );
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

        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata-0.4.0.Final-1", mParams1.get( 0 ) );
        assertEquals( "org.jbpm-jbpm-parent-metadata-0.4.0.Final-1", mParams2.get( 0 ) );
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

        // a. verify response from getBuild call

        // if we know the type (KojiBuildInfo), parse the Map to it
        Parser<KojiBuildInfo> parser_KojiBuildInfo = Registry.getInstance().getParser( KojiBuildInfo.class );
        KojiBuildInfo kojiBuildInfo = parser_KojiBuildInfo.parse( data1 );
        assertEquals( 48475, kojiBuildInfo.getPackageId() );
        assertEquals( 513598, kojiBuildInfo.getBuildId() );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", kojiBuildInfo.getPackageName() );

        // if we do not know the type, access Map directly
        Map<String, Object> data1Map = (Map) data1;
        assertEquals( 48475, data1Map.get( "package_id" ) );
        assertEquals( 513598, data1Map.get( "build_id" ) );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", data1Map.get( "package_name" ) );


        // b. verify response from listTags call

        List<Object> data2List = (List) data2;
        assertEquals( 4, data2List.size() );

        // if we know the type (KojiTagInfo) in the list, parse the element to it
        Parser<KojiTagInfo> parser_KojiTagInfo = Registry.getInstance().getParser( KojiTagInfo.class );
        KojiTagInfo kojiTagInfo = parser_KojiTagInfo.parse( data2List.get( 0 ) );
        assertEquals( "jb-bxms-6.3-candidate", kojiTagInfo.getName() );
        assertEquals( 8829, kojiTagInfo.getId() );

        // if we do not know the type, access List directly
        Object data2_1 = data2List.get( 0 );
        assertTrue( data2_1 instanceof Map );
        Map<String, Object> data2_1Map = (Map) data2_1;
        assertEquals( "jb-bxms-6.3-candidate", data2_1Map.get( "name" ) );
        assertEquals( 8829, data2_1Map.get( "id" ) );
    }

}
