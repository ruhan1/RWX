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
