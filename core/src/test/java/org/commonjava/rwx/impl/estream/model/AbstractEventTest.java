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
package org.commonjava.rwx.impl.estream.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.util.List;

public abstract class AbstractEventTest<T>
{

    @Test
    public void equality()
    {
        int count = 0;
        for ( final Event<T>[] instances : constructEqualInstanceSets() )
        {
            assertEquals( "Equality test " + count + " failed.", instances[0], instances[1] );
            count++;
        }
    }

    @Test
    public void inequality()
    {
        int count = 0;
        for ( final Event<T>[] instances : constructUnequalInstanceSets() )
        {
            assertFalse( "Inequality test " + count + " failed.", instances[0].equals( instances[1] ) );
            count++;
        }
    }

    @Test
    public void matchedHashCode()
    {
        int count = 0;
        for ( final Event<T>[] instances : constructEqualInstanceSets() )
        {
            assertEquals( "Hashcode match test " + count + " failed.", instances[0].hashCode(), instances[1].hashCode() );
            count++;
        }
    }

    @Test
    public void mismatchedHashCode()
    {
        int count = 0;
        for ( final Event<T>[] instances : constructUnequalInstanceSets() )
        {
            assertFalse( "Hashcode mismatch test " + count + " failed.",
                         instances[0].hashCode() == instances[1].hashCode() );
            count++;
        }
    }

    protected abstract List<Event<T>[]> constructEqualInstanceSets();

    protected abstract List<Event<T>[]> constructUnequalInstanceSets();

    protected void assertEvent( final Event<T> event, final EventType eType, final T index, final Object value,
                                final ValueType vType, final boolean expectSuccess )
    {
        assertEquals( expectSuccess, event.eventEquals( eType, index, value, vType ) );
    }

}
