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

import static org.junit.Assert.assertNull;

import org.commonjava.rwx.error.CoercionException;
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
        final Object result = fromString( null );
        assertNull( "Expected null result. Got: '" + result + "'", result );
    }

    protected abstract ValueType type();

    @Test
    public abstract void toStringStandardConversions()
        throws Throwable;

    @Test
    public abstract void fromStringStandardConversions()
        throws Throwable;

}
