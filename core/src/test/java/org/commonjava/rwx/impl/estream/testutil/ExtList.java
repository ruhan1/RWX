/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
