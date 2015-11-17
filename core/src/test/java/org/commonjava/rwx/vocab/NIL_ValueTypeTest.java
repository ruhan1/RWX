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
