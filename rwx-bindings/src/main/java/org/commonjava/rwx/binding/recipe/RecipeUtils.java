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

package org.commonjava.rwx.binding.recipe;

import org.commonjava.rwx.binding.error.BindException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class RecipeUtils
{

    private RecipeUtils()
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

    public static Map<Class<?>, Recipe<?>> mapRecipesByClass( final Collection<Recipe<?>> recipes )
        throws BindException
    {
        final Map<Class<?>, Recipe<?>> result = new HashMap<Class<?>, Recipe<?>>();
        for ( final Recipe<?> recipe : recipes )
        {
            result.put( recipe.getObjectType(), recipe );
        }

        return result;
    }

}
