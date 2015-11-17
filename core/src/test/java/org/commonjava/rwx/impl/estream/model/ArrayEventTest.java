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
package org.commonjava.rwx.impl.estream.model;

import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.impl.estream.testutil.ExtListOfArrays;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.util.List;

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

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<Integer>[]> constructEqualInstanceSets()
    {
        return new ExtListOfArrays<Event<Integer>>( 2 ).with( new ArrayEvent( EventType.START_ARRAY ),
                                                              new ArrayEvent( EventType.START_ARRAY ) )
                                                       .with( new ArrayEvent( 0 ), new ArrayEvent( 0 ) )
                                                       .with( new ArrayEvent( 0, "foo", ValueType.STRING ),
                                                              new ArrayEvent( 0, "foo", ValueType.STRING ) )
                                                       .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ),
                                                              new ArrayEvent( EventType.END_ARRAY_ELEMENT ) )
                                                       .with( new ArrayEvent( EventType.END_ARRAY ),
                                                              new ArrayEvent( EventType.END_ARRAY ) );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<Integer>[]> constructUnequalInstanceSets()
    {
        return new ExtListOfArrays<Event<Integer>>( 2 ).with( new ArrayEvent( EventType.START_ARRAY ),
                                                              new ArrayEvent( EventType.END_ARRAY ) )
                                                       .with( new ArrayEvent( 0 ), new ArrayEvent( 1 ) )
                                                       .with( new ArrayEvent( 0, "foo", ValueType.STRING ),
                                                              new ArrayEvent( 0, "foo2", ValueType.STRING ) )
                                                       .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ),
                                                              new ArrayEvent( EventType.END_ARRAY ) )
                                                       .with( new ArrayEvent( EventType.END_ARRAY ), new ArrayEvent( 0 ) )
                                                       .with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ),
                                                              new ArrayEvent( EventType.START_ARRAY ) )
                                                       .with( new ArrayEvent( EventType.START_ARRAY ),
                                                              new ArrayEvent( 0 ) );
    }

}
