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
