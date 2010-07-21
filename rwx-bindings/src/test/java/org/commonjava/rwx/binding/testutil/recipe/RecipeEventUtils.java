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

package org.commonjava.rwx.binding.testutil.recipe;

import org.commonjava.rwx.impl.estream.model.ArrayEvent;
import org.commonjava.rwx.impl.estream.model.Event;
import org.commonjava.rwx.impl.estream.model.ParameterEvent;
import org.commonjava.rwx.impl.estream.model.StructEvent;
import org.commonjava.rwx.impl.estream.model.ValueEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.ExtMap;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;


import java.util.Collection;
import java.util.Map;

public final class RecipeEventUtils
{

    private RecipeEventUtils()
    {
    }

    public static Collection<Event<?>> parameter( final int key, final Object value, final ValueType type )
    {
        final ExtList<Event<?>> result = new ExtList<Event<?>>( new ParameterEvent( key ) );

        result.withAll( endParameter( key, value, type ) );

        return result;
    }

    public static Collection<Event<?>> endParameter( final int key, final Object value, final ValueType type )
    {
        return new ExtList<Event<?>>( new ValueEvent( value, type ), new ParameterEvent( key, value, type ),
                                      new ParameterEvent() );
    }

    public static Collection<Event<?>> stringStruct( final ExtMap<String, String> map )
    {
        final ExtList<Event<?>> result = new ExtList<Event<?>>();
        result.with( new StructEvent( EventType.START_STRUCT ) );

        for ( final Map.Entry<String, String> entry : map.entrySet() )
        {
            result.with( new StructEvent( entry.getKey() ) );

            result.with( new ValueEvent( entry.getValue(), entry.getValue() == null ? ValueType.NIL : ValueType.STRING ) );

            result.with( new StructEvent( entry.getKey(), entry.getValue(), entry.getValue() == null ? ValueType.NIL
                            : ValueType.STRING ) );

            result.with( new StructEvent( EventType.END_STRUCT_MEMBER ) );
        }

        result.with( new StructEvent( EventType.END_STRUCT ) );

        return result;
    }

    public static Collection<Event<?>> stringArray( final ExtList<String> list )
    {
        final ExtList<Event<?>> result = new ExtList<Event<?>>();
        result.with( new ArrayEvent( EventType.START_ARRAY ) );

        int i = 0;
        for ( final String entry : list )
        {
            result.with( new ArrayEvent( i ) );

            result.with( new ValueEvent( entry, entry == null ? ValueType.NIL : ValueType.STRING ) );

            result.with( new ArrayEvent( i, entry, entry == null ? ValueType.NIL : ValueType.STRING ) );

            result.with( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) );

            i++;
        }

        result.with( new ArrayEvent( EventType.END_ARRAY ) );

        return result;
    }
}
