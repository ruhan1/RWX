package org.commonjava.rwx2.core;

import org.commonjava.rwx2.api.RWXMapper;
import org.commonjava.rwx2.core.simple.RequestWithOneParam;
import org.commonjava.rwx2.core.simple.SimpleRequest;
import org.commonjava.rwx2.core.simple.SimpleResponse;
import org.junit.Test;

import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 7/19/17.
 */
public class RWXMapperTest
                extends AbstractTest
{
    @Test
    public void simpleRequestTest() throws Exception
    {
        String request = new RWXMapper().renderRequest( new SimpleRequest() );
        String expected = getXMLStringIgnoreFormat( "simpleRequest" );
        assertEquals( expected, formalizeXMLString( request ) );
    }

    @Test
    public void requestWithOneParamTest() throws Exception
    {
        RequestWithOneParam requst = new RequestWithOneParam();
        requst.setValue( "test" );
        String request = new RWXMapper().renderRequest( requst );
        String expected = getXMLStringIgnoreFormat( "requestWithOneParam" );
        assertEquals( expected, formalizeXMLString( request ) );
    }

    @Test
    public void simpleResponseTest() throws Exception
    {
        InputStream stream = getXMLStream( "simpleResponse" );
        SimpleResponse response = new RWXMapper().parseResponse( stream, SimpleResponse.class );
        assertEquals( 18.24668429131D, response.getValue() );
    }

}

