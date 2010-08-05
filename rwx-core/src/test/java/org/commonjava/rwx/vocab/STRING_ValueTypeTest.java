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


public class STRING_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "foo", fromString( "foo" ) );
        assertEquals( "1.0", fromString( "1.0" ) );
        assertEquals( "true", fromString( "true" ) );
    }

    @Override
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "foo", toString( "foo" ) );
        assertEquals( "1.0", toString( new Double( 1.0 ) ) );
        assertEquals( "true", toString( true ) );
    }

    @Override
    protected ValueType type()
    {
        return ValueType.STRING;
    }

}
