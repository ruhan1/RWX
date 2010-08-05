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

package org.commonjava.rwx.impl.estream.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.commonjava.rwx.impl.estream.model.Event;
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
