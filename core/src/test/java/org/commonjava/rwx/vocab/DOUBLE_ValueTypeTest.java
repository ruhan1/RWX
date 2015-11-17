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

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DOUBLE_ValueTypeTest
    extends AbstractValueTypeTest
{

    @Override
    protected ValueType type()
    {
        return ValueType.DOUBLE;
    }

    @Override
    public void toStringStandardConversions()
        throws CoercionException
    {
        assertEquals( "1.0", toString( new Integer( 1 ) ) );
        assertEquals( "1.0", toString( new Long( 1 ) ) );
        assertEquals( "1.0", toString( new Float( 1.0 ) ) );
        assertEquals( "1.0", toString( new Double( 1.0 ) ) );
    }

    @Override
    public void fromStringStandardConversions()
        throws CoercionException
    {
        assertEquals( 1.0, fromString( Integer.toString( 1 ) ) );
        assertEquals( 1.0, fromString( Long.toString( 1 ) ) );
        assertEquals( 1.0, fromString( Float.toString( 1.0f ) ) );
        assertEquals( 1.0, fromString( Double.toString( 1.0d ) ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenDateStringPassedIn()
        throws CoercionException
    {
        fromString( new SimpleDateFormat().format( new Date() ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenNonDoublePassedIn()
        throws CoercionException
    {
        fromString( "foo" );
    }

}
