/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
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
