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
import org.commonjava.rwx.vocab.ValueType;


public class NIL_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    protected ValueType type()
    {
        return ValueType.NIL;
    }

    @Override
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertNull( toString( new Integer( 1 ) ) );
        assertNull( toString( "foo" ) );
        assertNull( toString( null ) );
    }

    @Override
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertNull( fromString( "1" ) );
        assertNull( fromString( "foo" ) );
        assertNull( fromString( null ) );
    }

}
