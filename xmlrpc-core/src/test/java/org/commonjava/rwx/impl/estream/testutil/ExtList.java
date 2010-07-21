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

package org.commonjava.rwx.impl.estream.testutil;

import java.util.ArrayList;
import java.util.Collection;

public class ExtList<T>
    extends ArrayList<T>
{

    private static final long serialVersionUID = 1L;

    public ExtList()
    {
    }

    public ExtList( final T... values )
    {
        if ( values != null )
        {
            for ( final T value : values )
            {
                add( value );
            }
        }
    }

    public ExtList<T> with( final T value )
    {
        add( value );
        return this;
    }

    public ExtList<T> withAll( final Collection<T> values )
    {
        if ( values != null && !values.isEmpty() )
        {
            addAll( values );
        }
        return this;
    }

    public ExtList<T> withAll( final T... values )
    {
        if ( values != null && values.length > 0 )
        {
            for ( final T t : values )
            {
                add( t );
            }
        }

        return this;
    }

}
