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
import org.commonjava.rwx.test.AbstractTest;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 4/9/18.
 */
public class I8MapperTest
                extends AbstractTest
{

    @Test
    public void renderRequestTest() throws Exception
    {
        I8Request request = new I8Request();
        request.setIntValue( 1000 );
        request.setLongValue( 1000L );
        String rendered = new RWXMapper().render( request );
        //System.out.println( ">>> " + rendered );

        String expected = getXMLStringIgnoreFormat( "i8Request" );
        assertEquals( expected, formalizeXMLString( rendered ) );
    }

    @Test
    public void roundTrip_ResponseTest() throws Exception
    {
        I8Response response = new I8Response();
        response.setIntValue( 1000 );
        response.setLongValue( 2000L );
        response.setLongPrimitive( 3000L );
        String rendered = new RWXMapper().render( response );
        //System.out.println( ">>> " + rendered );

        I8Response parsed = new RWXMapper().parse( new ByteArrayInputStream( rendered.getBytes() ), I8Response.class );
        assertEquals( parsed.getIntValue(), response.getIntValue() );
        assertEquals( parsed.getLongValue(), response.getLongValue() );
        assertEquals( parsed.getLongPrimitive(), response.getLongPrimitive() );
    }

}

