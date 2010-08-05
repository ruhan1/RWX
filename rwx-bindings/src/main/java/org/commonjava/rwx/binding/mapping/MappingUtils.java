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
