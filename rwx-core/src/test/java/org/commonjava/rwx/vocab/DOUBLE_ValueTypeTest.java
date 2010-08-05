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

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DOUBLE_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    protected ValueType type()
    {
        return ValueType.DOUBLE;
    }

    @Override
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "1.0", toString( new Integer( 1 ) ) );
        assertEquals( "1.0", toString( new Long( 1 ) ) );
        assertEquals( "1.0", toString( new Float( 1.0 ) ) );
        assertEquals( "1.0", toString( new Double( 1.0 ) ) );
    }

    @Override
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertEquals( 1.0, fromString( Integer.toString( 1 ) ) );
        assertEquals( 1.0, fromString( Long.toString( 1 ) ) );
        assertEquals( 1.0, fromString( Float.toString( 1.0f ) ) );
        assertEquals( 1.0, fromString( Double.toString( 1.0d ) ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenDateStringPassedIn()
        throws CoercionException
    {
        fromString( new SimpleDateFormat().format( new Date() ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenNonDoublePassedIn()
        throws CoercionException
    {
        fromString( "foo" );
    }

}
