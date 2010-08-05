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

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.util.ArrayList;

public class ARRAY_ValueTypeTest
{

    @Test( expected = CoercionException.class )
    public void toStringThrowsError()
        throws CoercionException
    {
        ValueType.ARRAY.coercion().toString( new ArrayList<Object>() );
    }

    @Test( expected = CoercionException.class )
    public void fromStringThrowsError()
        throws CoercionException
    {
        ValueType.ARRAY.coercion().fromString( "foo" );
    }

}
