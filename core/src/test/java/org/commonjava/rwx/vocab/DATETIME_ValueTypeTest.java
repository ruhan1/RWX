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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DATETIME_ValueTypeTest
    extends AbstractValueTypeTest
{

    private static final String DATE = "20100715T112255";

    private Date date()
        throws ParseException
    {
        return new SimpleDateFormat( "yyyyMMdd'T'HHmmss" ).parse( DATE );
    }

    @Override
    protected ValueType type()
    {
        return ValueType.DATETIME;
    }

    @Override
    @Test
    public void toStringStandardConversions()
        throws CoercionException, ParseException
    {
        assertEquals( DATE, toString( date() ) );
    }

    @Override
    @Test
    public void fromStringStandardConversions()
        throws CoercionException, ParseException
    {
        assertEquals( date(), fromString( DATE ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenLongPassedIn()
        throws CoercionException
    {
        fromString( Long.toString( Long.MAX_VALUE ) );
    }

    @Test( expected = CoercionException.class )
    public void fromStringErrorsOutWhenNonDatePassedIn()
        throws CoercionException
    {
        fromString( "1.0" );
    }

}
