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

import static org.junit.Assert.assertNull;

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


public abstract class AbstractValueTypeTest
{

    protected Object fromString( final String str )
        throws CoercionException
    {
        return type().coercion().fromString( str );
    }

    protected String toString( final Object val )
        throws CoercionException
    {
        return type().coercion().toString( val );
    }

    @Test
    public final void toStringReturnNullWhenInputIsNull()
        throws Throwable
    {
        assertNull( toString( null ) );
    }

    @Test
    public final void fromStringReturnNullWhenInputIsNull()
        throws Throwable
    {
        assertNull( fromString( null ) );
    }

    protected abstract ValueType type();

    @Test
    public abstract void toStringStandardConversions()
        throws Throwable;

    @Test
    public abstract void fromStringStandardConversions()
        throws Throwable;

}
