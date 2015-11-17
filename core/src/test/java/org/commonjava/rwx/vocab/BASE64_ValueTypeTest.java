/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.vocab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.codec.binary.Base64;
import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.testutil.ValueUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;

public class BASE64_ValueTypeTest
    extends AbstractValueTypeTest
{

    private static final byte[] VALUE = "20100715T112255".getBytes();

    private String b64()
    {
        return new String( Base64.encodeBase64( VALUE ) );
    }

    @Override
    protected ValueType type()
    {
        return ValueType.BASE64;
    }

    @Override
    @Test
    public void toStringStandardConversions()
        throws CoercionException, ParseException
    {
        assertEquals( b64(), toString( VALUE ) );
    }

    @Override
    @Test
    public void fromStringStandardConversions()
        throws CoercionException, ParseException
    {
        assertByteArray( VALUE, (byte[]) fromString( b64() ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenNonBase64PassedIn()
        throws CoercionException
    {
        final byte[] result = (byte[]) fromString( "@@!!" );
        System.out.println( "result: " + ValueUtils.renderByteArray( result ) );
    }

    private void assertByteArray( final byte[] check, final byte[] value )
    {
        if ( !Arrays.equals( check, value ) )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( "Expected:\n\n" ).append( ValueUtils.renderByteArray( check ) );
            sb.append( "\n\nActual:\n\n" ).append( ValueUtils.renderByteArray( value ) );
            sb.append( "\n\nByte arrays do not match." );

            fail( sb.toString() );
        }
    }

}
