/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
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
