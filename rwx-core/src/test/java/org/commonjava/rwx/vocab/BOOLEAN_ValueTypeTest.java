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
import static org.junit.Assert.assertFalse;

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BOOLEAN_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    protected ValueType type()
    {
        return ValueType.BOOLEAN;
    }

    @Override
    @Test
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "true", toString( new Boolean( true ) ) );
        assertEquals( "true", toString( true ) );
        assertEquals( "false", toString( new Boolean( false ) ) );
        assertEquals( "false", toString( false ) );
    }

    @Override
    @Test
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertEquals( true, fromString( "true" ) );
        assertEquals( false, fromString( "false" ) );
        assertEquals( false, fromString( "foo" ) );
    }

    @Test
    public void toStringReturnFalseWhenNonBooleanPassedIn()
        throws CoercionException
    {
        assertEquals( "false", toString( new ArrayList<Integer>() ) );
    }

    @Test
    public void fromStringReturnFalseWhenNonBooleanPassedIn()
        throws CoercionException
    {
        assertFalse( (Boolean) fromString( new SimpleDateFormat().format( new Date() ) ) );
    }

}
