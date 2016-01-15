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
package org.commonjava.rwx.binding.mapping;

import org.commonjava.rwx.binding.error.BindException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class MappingUtils
{

    private MappingUtils()
    {
    }

    public static String[] toStringArray( final Object... src )
    {
        final String[] result = new String[src.length];
        for ( int i = 0; i < src.length; i++ )
        {
            result[i] = src[i] == null ? null : String.valueOf( src[i] );
        }

        return result;
    }

    public static Integer[] toIntegerArray( final int... src )
    {
        final Integer[] result = new Integer[src.length];
        for ( int i = 0; i < src.length; i++ )
        {
            result[i] = src[i];
        }

        return result;
    }

    public static Map<Class<?>, Mapping<?>> mapRecipesByClass( final Collection<Mapping<?>> recipes )
        throws BindException
    {
        final Map<Class<?>, Mapping<?>> result = new HashMap<Class<?>, Mapping<?>>();
        for ( final Mapping<?> recipe : recipes )
        {
            result.put( recipe.getObjectType(), recipe );
        }

        return result;
    }

}
