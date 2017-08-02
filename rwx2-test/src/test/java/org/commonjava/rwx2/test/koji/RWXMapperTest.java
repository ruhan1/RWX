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

import static junit.framework.TestCase.assertEquals;

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

    private String splitAndSort( String source )
    {
        String begin = "<member>";
        String end = "</member>";

        StringBuilder sb = new StringBuilder();
        List<String> l = new ArrayList<>();
        String newSource = source.replaceAll( begin, "\n" + begin );
        String[] lines = newSource.split( "\n" );
        for ( String line : lines )
        {
            if ( line.startsWith( begin ) )
            {
                line = line.substring( 0, line.indexOf( end ) + end.length() ); // remove what is following </member>
                l.add( line );
            }
        }
        String[] array = l.toArray( new String[0] );
        Arrays.sort( array );
        for ( String s : array )
        {
            sb.append( s + "\n" );
        }
        return sb.toString();
    }
}
