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

package org.commonjava.rwx.impl.estream.model;

import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.impl.estream.model.Event;
import org.commonjava.rwx.impl.estream.model.ValueEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtListOfArrays;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.util.List;

public class ValueEventTest
    extends AbstractEventTest<Void>
{

    @Test
    public void typeIs_VALUE()
    {
        assertEquals( EventType.VALUE, new ValueEvent( null, null ).getEventType() );
    }

    @Test
    public void matchValueType()
    {
        assertEquals( ValueType.STRING, new ValueEvent( null, ValueType.STRING ).getType() );
    }

    @Test
    public void matchValue()
    {
        assertEquals( "foo", new ValueEvent( "foo", ValueType.STRING ).getValue() );
    }

    @Test
    public void matchValueEventEqualsWithGenericEventEquals()
    {
        final ValueEvent event = new ValueEvent( "foo", ValueType.STRING );

        final boolean valEq = event.valueEventEquals( "foo", ValueType.STRING );
        final boolean eq = event.eventEquals( EventType.VALUE, null, "foo", ValueType.STRING );

        assertEquals( valEq, eq );
    }

    @Test
    public void match()
    {
        assertEvent( new ValueEvent( "foo", ValueType.STRING ), EventType.VALUE, null, "foo", ValueType.STRING, true );
    }

    @Test
    public void matchNulls()
    {
        assertEvent( new ValueEvent( null, ValueType.STRING ), EventType.VALUE, null, null, ValueType.STRING, true );
    }

    @Test
    public void valueMismatch()
    {
        assertEvent( new ValueEvent( "foo", ValueType.STRING ), EventType.VALUE, null, "bar", ValueType.STRING, false );
    }

    @Test
    public void valueMismatchNull()
    {
        assertEvent( new ValueEvent( "foo", ValueType.STRING ), EventType.VALUE, null, null, ValueType.STRING, false );
    }

    @Test
    public void eventTypeMismatch()
    {
        assertEvent( new ValueEvent( "foo", ValueType.STRING ), EventType.ARRAY_ELEMENT, null, "foo", ValueType.STRING,
                     false );
    }

    @Test
    public void typeMismatch_IntForString()
    {
        assertEvent( new ValueEvent( "foo", ValueType.STRING ), EventType.VALUE, null, 1, ValueType.INT, false );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<Void>[]> constructEqualInstanceSets()
    {
        return new ExtListOfArrays<Event<Void>>( 2 ).with( new ValueEvent( "foo", ValueType.STRING ),
                                                           new ValueEvent( "foo", ValueType.STRING ) );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<Void>[]> constructUnequalInstanceSets()
    {
        return new ExtListOfArrays<Event<Void>>( 2 ).with( new ValueEvent( "foo", ValueType.STRING ),
                                                           new ValueEvent( "bar", ValueType.STRING ) );
    }

}
