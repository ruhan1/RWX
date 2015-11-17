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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.commonjava.rwx.error.CoercionException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BOOLEAN_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    protected ValueType type()
    {
        return ValueType.BOOLEAN;
    }

    @Override
    @Test
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "1", toString( new Boolean( true ) ) );
        assertEquals( "1", toString( true ) );
        assertEquals( "0", toString( new Boolean( false ) ) );
        assertEquals( "0", toString( false ) );
    }

    @Override
    @Test
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertEquals( true, fromString( "1" ) );
        assertEquals( false, fromString( "0" ) );
        assertEquals( false, fromString( "foo" ) );
    }

    @Test
    public void toStringReturnFalseWhenNonBooleanPassedIn()
        throws CoercionException
    {
        assertEquals( "0", toString( new ArrayList<Integer>() ) );
    }

    @Test
    public void fromStringReturnFalseWhenNonBooleanPassedIn()
        throws CoercionException
    {
        assertFalse( (Boolean) fromString( new SimpleDateFormat().format( new Date() ) ) );
    }

}
