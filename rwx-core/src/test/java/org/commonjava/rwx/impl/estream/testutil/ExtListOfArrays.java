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

package org.commonjava.rwx.impl.estream.testutil;

import java.util.ArrayList;

public class ExtListOfArrays<T>
    extends ArrayList<T[]>
{

    private static final long serialVersionUID = 1L;

    private final int arraySz;

    public ExtListOfArrays( final int arraySz )
    {
        this.arraySz = arraySz;
    }

    public ExtListOfArrays<T> with( final T... values )
    {
        if ( values == null || values.length != arraySz )
        {
            throw new IllegalArgumentException( "Array input must be of size: " + arraySz );
        }

        add( values );
        return this;
    }

}
