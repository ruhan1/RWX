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

public class ArrayEventTest
    extends AbstractEventTest<Integer>
{

    @Test
    public void matchArrayElementInfo()
    {
        final ArrayEvent event = new ArrayEvent( 0, "foo", ValueType.STRING );

        assertEquals( ValueType.STRING, event.getValueType() );
        assertEquals( "foo", event.getValue() );
        assertEquals( 0, event.getIndex() );
        assertEquals( EventType.ARRAY_ELEMENT, event.getEventType() );
    }

    @Test
    public void matchArrayElementStartInfo()
    {
        final ArrayEvent event = new ArrayEvent( 0 );

        assertEquals( EventType.START_ARRAY_ELEMENT, event.getEventType() );
        assertEquals( 0, event.getIndex() );
    }

    @Test
    public void matchStartArray()
    {
        assertEvent( new ArrayEvent( EventType.START_ARRAY ), EventType.START_ARRAY, null, null, null, true );
    }

    @Test
    public void matchEndArray()
    {
        assertEvent( new ArrayEvent( EventType.END_ARRAY ), EventType.END_ARRAY, null, null, null, true );
    }

    @Test
    public void matchEndArrayElement()
    {
        assertEvent( new ArrayEvent( EventType.END_ARRAY_ELEMENT ), EventType.END_ARRAY_ELEMENT, null, null, null, true );
    }

    @Test
    public void mismatchStartVsEndArrayTypes()
    {
        assertEvent( new ArrayEvent( EventType.START_ARRAY ), EventType.END_ARRAY, null, null, null, false );
    }

    @Test
    public void mismatchStartVsEndArrayElementTypes()
    {
        assertEvent( new ArrayEvent( EventType.START_ARRAY ), EventType.END_ARRAY_ELEMENT, null, null, null, false );
    }

    @Test
    public void mismatchEndVsEndArrayElementTypes()
    {
        assertEvent( new ArrayEvent( EventType.END_ARRAY ), EventType.END_ARRAY_ELEMENT, null, null, null, false );
    }

    @Test
    public void matchElementIndex()
    {
        assertEvent( new ArrayEvent( 0 ), EventType.START_ARRAY_ELEMENT, 0, null, null, true );
    }

    @Test
    public void mismatchElementIndex()
    {
        assertEvent( new ArrayEvent( 0 ), EventType.START_ARRAY_ELEMENT, 1, null, null, false );
    }

    @Test
    public void matchElementValue()
    {
        assertEvent( new ArrayEvent( 0, "foo", ValueType.STRING ), EventType.ARRAY_ELEMENT, 0, "foo", ValueType.STRING,
                     true );
    }

    @Test
    public void mismatchElementValue()
    {
        assertEvent( new ArrayEvent( 0, "foo", ValueType.STRING ), EventType.ARRAY_ELEMENT, 0, "bar", ValueType.STRING,
                     false );
    }

    @Test
    public void matchElementValueType()
    {
        assertEvent( new ArrayEvent( 0, "foo", ValueType.STRING ), EventType.ARRAY_ELEMENT, 0, "foo", ValueType.STRING,
                     true );
    }

    @Test
    public void mismatchElementValueType()
    {
        assertEvent( new ArrayEvent( 0, "foo", ValueType.STRING ), EventType.ARRAY_ELEMENT, 0, "bar", ValueType.BASE64,
                     false );
    }

    @Test( expected = IllegalArgumentException.class )
    public void errorOnNonArrayEventType()
    {
        new ArrayEvent( EventType.START_STRUCT );
    }

    @Test( expected = IllegalArgumentException.class )
    public void errorOnArrayElementEventType()
    {
        new ArrayEvent( EventType.ARRAY_ELEMENT );
    }

    @Test( expected = IllegalArgumentException.class )
    public void errorOnArrayElementStartEventType()
    {
        new ArrayEvent( EventType.START_ARRAY_ELEMENT );
    }

}
