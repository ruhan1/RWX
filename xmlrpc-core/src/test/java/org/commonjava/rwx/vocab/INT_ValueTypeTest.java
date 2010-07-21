/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.commonjava.rwx.vocab;

import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


public class INT_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    protected ValueType type()
    {
        return ValueType.INT;
    }

    @Override
    @Test
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "1", toString( 1 ) );
    }

    @Override
    @Test
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertEquals( 1, fromString( "1" ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenLongPassedIn()
        throws CoercionException
    {
        fromString( Long.toString( Long.MAX_VALUE ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenDoublePassedIn()
        throws CoercionException
    {
        fromString( "1.0" );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenNonIntPassedIn()
        throws CoercionException
    {
        fromString( "foo" );
    }

}
