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

package com.redhat.xmlrpc.impl.estream.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

public class ParameterEventTest
    extends AbstractEventTest<Integer>
{

    @Test
    public void matchParameterValueInfo()
    {
        final ParameterEvent event = new ParameterEvent( 0, "foo", ValueType.STRING );

        assertEquals( ValueType.STRING, event.getValueType() );
        assertEquals( "foo", event.getValue() );
        assertEquals( 0, event.getIndex() );
        assertEquals( EventType.PARAMETER, event.getEventType() );
    }

    @Test
    public void matchParameterStartInfo()
    {
        final ParameterEvent event = new ParameterEvent( 0 );

        assertEquals( EventType.START_PARAMETER, event.getEventType() );
        assertEquals( 0, event.getIndex() );
    }

    @Test
    public void matchEndParameter()
    {
        assertEvent( new ParameterEvent(), EventType.END_PARAMETER, null, null, null, true );
    }

    @Test
    public void mismatchStartVsEndParameterTypes()
    {
        assertEvent( new ParameterEvent( 0 ), EventType.END_STRUCT, null, null, null, false );
    }

    @Test
    public void mismatchParameterIndex()
    {
        assertEvent( new ParameterEvent( 0 ), EventType.START_PARAMETER, 1, null, null, false );
    }

    @Test
    public void matchParameterValue()
    {
        assertEvent( new ParameterEvent( 0, "foo", ValueType.STRING ), EventType.PARAMETER, 0, "foo", ValueType.STRING,
                     true );
    }

    @Test
    public void mismatchParameterValue()
    {
        assertEvent( new ParameterEvent( 0, "foo", ValueType.STRING ), EventType.PARAMETER, 0, "bar", ValueType.STRING,
                     false );
    }

    @Test
    public void matchParameterValueType()
    {
        assertEvent( new ParameterEvent( 0, "foo", ValueType.STRING ), EventType.PARAMETER, 0, "foo", ValueType.STRING,
                     true );
    }

    @Test
    public void mismatchParameterValueType()
    {
        assertEvent( new ParameterEvent( 0, "foo", ValueType.STRING ), EventType.PARAMETER, 0, "bar", ValueType.BASE64,
                     false );
    }

}
