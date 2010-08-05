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
