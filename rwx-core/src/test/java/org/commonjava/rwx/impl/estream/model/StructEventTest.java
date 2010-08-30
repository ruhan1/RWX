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

import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtListOfArrays;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;
import org.junit.Test;


import java.util.List;

public class StructEventTest
    extends AbstractEventTest<String>
{

    @Test
    public void matchStructMemberInfo()
    {
        final StructEvent event = new StructEvent( "key", "foo", ValueType.STRING );

        assertEquals( ValueType.STRING, event.getValueType() );
        assertEquals( "foo", event.getValue() );
        assertEquals( "key", event.getKey() );
        assertEquals( EventType.STRUCT_MEMBER, event.getEventType() );
    }

    @Test
    public void matchStructMemberStartInfo()
    {
        final StructEvent event = new StructEvent( "key" );

        assertEquals( EventType.START_STRUCT_MEMBER, event.getEventType() );
        assertEquals( "key", event.getKey() );
    }

    @Test
    public void matchStartStruct()
    {
        assertEvent( new StructEvent( EventType.START_STRUCT ), EventType.START_STRUCT, null, null, null, true );
    }

    @Test
    public void matchEndStruct()
    {
        assertEvent( new StructEvent( EventType.END_STRUCT ), EventType.END_STRUCT, null, null, null, true );
    }

    @Test
    public void matchEndMemberStruct()
    {
        assertEvent( new StructEvent( EventType.END_STRUCT_MEMBER ), EventType.END_STRUCT_MEMBER, null, null, null,
                     true );
    }

    @Test
    public void mismatchStartVsEndStructTypes()
    {
        assertEvent( new StructEvent( EventType.START_STRUCT ), EventType.END_STRUCT, null, null, null, false );
    }

    @Test
    public void mismatchStartVsEndMemberStructTypes()
    {
        assertEvent( new StructEvent( EventType.START_STRUCT ), EventType.END_STRUCT_MEMBER, null, null, null, false );
    }

    @Test
    public void mismatchEndVsEndMemberStructTypes()
    {
        assertEvent( new StructEvent( EventType.END_STRUCT ), EventType.END_STRUCT_MEMBER, null, null, null, false );
    }

    @Test
    public void matchMemberKey()
    {
        assertEvent( new StructEvent( "key" ), EventType.START_STRUCT_MEMBER, "key", null, null, true );
    }

    @Test
    public void mismatchMemberKey()
    {
        assertEvent( new StructEvent( "key" ), EventType.START_STRUCT_MEMBER, "key2", null, null, false );
    }

    @Test
    public void matchMemberValue()
    {
        assertEvent( new StructEvent( "key", "foo", ValueType.STRING ), EventType.STRUCT_MEMBER, "key", "foo",
                     ValueType.STRING, true );
    }

    @Test
    public void mismatchMemberValue()
    {
        assertEvent( new StructEvent( "key", "foo", ValueType.STRING ), EventType.STRUCT_MEMBER, "key", "bar",
                     ValueType.STRING, false );
    }

    @Test
    public void matchMemberValueType()
    {
        assertEvent( new StructEvent( "key", "foo", ValueType.STRING ), EventType.STRUCT_MEMBER, "key", "foo",
                     ValueType.STRING, true );
    }

    @Test
    public void mismatchMemberValueType()
    {
        assertEvent( new StructEvent( "key", "foo", ValueType.STRING ), EventType.STRUCT_MEMBER, "key", "bar",
                     ValueType.BASE64, false );
    }

    @Test( expected = IllegalArgumentException.class )
    public void errorOnNonStructEventType()
    {
        new StructEvent( EventType.ARRAY_ELEMENT );
    }

    @Test( expected = IllegalArgumentException.class )
    public void errorOnStructMemberEventType()
    {
        new StructEvent( EventType.STRUCT_MEMBER );
    }

    @Test( expected = IllegalArgumentException.class )
    public void errorOnStructMemberStartEventType()
    {
        new StructEvent( EventType.START_STRUCT_MEMBER );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<String>[]> constructEqualInstanceSets()
    {
        return new ExtListOfArrays<Event<String>>( 2 ).with( new StructEvent( EventType.START_STRUCT ),
                                                             new StructEvent( EventType.START_STRUCT ) )
                                                      .with( new StructEvent( "key" ), new StructEvent( "key" ) )
                                                      .with( new StructEvent( EventType.END_STRUCT_MEMBER ),
                                                             new StructEvent( EventType.END_STRUCT_MEMBER ) )
                                                      .with( new StructEvent( "key", "foo", ValueType.STRING ),
                                                             new StructEvent( "key", "foo", ValueType.STRING ) )
                                                      .with( new StructEvent( EventType.END_STRUCT ),
                                                             new StructEvent( EventType.END_STRUCT ) );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected List<Event<String>[]> constructUnequalInstanceSets()
    {
        return new ExtListOfArrays<Event<String>>( 2 ).with( new StructEvent( EventType.START_STRUCT ),
                                                             new StructEvent( EventType.END_STRUCT ) )
                                                      .with( new StructEvent( "key" ), new StructEvent( "key2" ) )
                                                      .with( new StructEvent( EventType.END_STRUCT_MEMBER ),
                                                             new StructEvent( EventType.END_STRUCT ) )
                                                      .with( new StructEvent( "key", "foo", ValueType.STRING ),
                                                             new StructEvent( "key", "foo2", ValueType.STRING ) )
                                                      .with( new StructEvent( "key" ),
                                                             new StructEvent( EventType.END_STRUCT ) );
    }

}
