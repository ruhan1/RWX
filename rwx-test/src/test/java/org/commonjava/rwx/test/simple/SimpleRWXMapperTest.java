package org.commonjava.rwx.test.simple;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.test.AbstractTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 7/19/17.
 */
public class SimpleRWXMapperTest
                extends AbstractTest
{
    @BeforeClass
    public static void register()
    {
        Registry.setInstance( new org.commonjava.rwx.test.generated.Test_Registry() );
    }

    @Test
    public void simpleRequestTest() throws Exception
    {
        String request = new RWXMapper().render( new SimpleRequest() );
        String expected = getXMLStringIgnoreFormat( "simpleRequest" );
        assertEquals( expected, formalizeXMLString( request ) );
    }

    @Test
    public void requestWithOneParamTest() throws Exception
    {
        RequestWithOneParam requst = new RequestWithOneParam();
        requst.setValue( "test" );
        String request = new RWXMapper().render( requst );
        String expected = getXMLStringIgnoreFormat( "requestWithOneParam" );
        assertEquals( expected, formalizeXMLString( request ) );
    }

    @Test
    public void simpleResponseTest() throws Exception
    {
        InputStream stream = getXMLStream( "simpleResponse" );
        SimpleResponse response = new RWXMapper().parse( stream, SimpleResponse.class );
        assertEquals( 18.24668429131D, response.getValue() );
    }

}

