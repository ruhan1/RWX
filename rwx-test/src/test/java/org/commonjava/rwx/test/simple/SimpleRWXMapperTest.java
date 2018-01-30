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
package org.commonjava.rwx.test.simple;

import org.commonjava.rwx.api.RWXMapper;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.test.AbstractTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 7/19/17.
 */
public class SimpleRWXMapperTest
                extends AbstractTest
{
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
    public void roundTrip_RequestWithOneArrayParamTest() throws Exception
    {
        RequestWithOneArrayParam requst = new RequestWithOneArrayParam();
        List<String> array = Arrays.asList( new String[] { "test1", "test2" } );
        requst.setArray( array );
        String request = new RWXMapper().render( requst );
        String expected = getXMLStringIgnoreFormat( "requestWithOneArrayParam" );
        assertEquals( expected, formalizeXMLString( request ) );

        RequestWithOneArrayParam parsed = new RWXMapper().parse( new ByteArrayInputStream( request.getBytes() ),
                                                                 RequestWithOneArrayParam.class );
        assertEquals( parsed.getArray().get( 0 ), array.get( 0 ) );
        assertEquals( parsed.getArray().get( 1 ), array.get( 1 ) );
    }

    @Test
    public void simpleResponseTest() throws Exception
    {
        InputStream stream = getXMLStream( "simpleResponse" );
        SimpleResponse response = new RWXMapper().parse( stream, SimpleResponse.class );
        assertEquals( 18.24668429131D, response.getValue() );
    }

}

