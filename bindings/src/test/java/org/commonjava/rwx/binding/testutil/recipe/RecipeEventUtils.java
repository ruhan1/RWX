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
package org.commonjava.rwx.binding.testutil.recipe;

import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class RecipeEventUtils
{

    private RecipeEventUtils()
    {
    }

    public static Collection<Event<?>> parameter( final int key, final Object value, final ValueType type )
    {
        final List<Event<?>> result = new ArrayList<>( Collections.singleton( new ParameterEvent( key ) ) );

        result.addAll( endParameter( key, value, type ) );

        return result;
    }

    public static Collection<Event<?>> endParameterWithConversion( final int key, final Object value,
                                                                   final ValueType type, final Object convertedValue,
                                                                   final ValueType convertedValueType )
    {
        return new ArrayList<>( Arrays.asList( new ValueEvent( value, type ),
                                                  new ParameterEvent( key, convertedValue, convertedValueType ),
                                                  new ParameterEvent() ) );
    }

    public static Collection<Event<?>> endParameter( final int key, final Object value, final ValueType type )
    {
        return new ArrayList<>( Arrays.asList( new ValueEvent( value, type ), new ParameterEvent( key, value, type ),
                                               new ParameterEvent() ) );
    }

    public static Collection<Event<?>> stringStruct( final Map<String, String> map )
    {
        final List<Event<?>> result = new ArrayList<>();
        result.add( new StructEvent( EventType.START_STRUCT ) );

        for ( final Map.Entry<String, String> entry : map.entrySet() )
        {
            result.add( new StructEvent( entry.getKey() ) );

            result.add(
                    new ValueEvent( entry.getValue(), entry.getValue() == null ? ValueType.NIL : ValueType.STRING ) );

            result.add( new StructEvent( entry.getKey(), entry.getValue(),
                                         entry.getValue() == null ? ValueType.NIL : ValueType.STRING ) );

            result.add( new StructEvent( EventType.END_STRUCT_MEMBER ) );
        }

        result.add( new StructEvent( EventType.END_STRUCT ) );

        return result;
    }

    public static Collection<Event<?>> stringArray( final List<String> list )
    {
        final List<Event<?>> result = new ArrayList<>();
        result.add( new ArrayEvent( EventType.START_ARRAY ) );

        int i = 0;
        for ( final String entry : list )
        {
            result.add( new ArrayEvent( i ) );

            result.add( new ValueEvent( entry, entry == null ? ValueType.NIL : ValueType.STRING ) );

            result.add( new ArrayEvent( i, entry, entry == null ? ValueType.NIL : ValueType.STRING ) );

            result.add( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) );

            i++;
        }

        result.add( new ArrayEvent( EventType.END_ARRAY ) );

        return result;
    }
}
