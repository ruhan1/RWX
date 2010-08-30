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

package org.commonjava.rwx.binding.testutil.recipe;

import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
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

    public static Collection<Event<?>> endParameterWithConversion( final int key, final Object value,
                                                                   final ValueType type, final Object convertedValue,
                                                                   final ValueType convertedValueType )
    {
        return new ExtList<Event<?>>( new ValueEvent( value, type ), new ParameterEvent( key, convertedValue,
                                                                                         convertedValueType ),
                                      new ParameterEvent() );
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
