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

package com.redhat.xmlrpc.binding.internal.reflect;

import static com.redhat.xmlrpc.binding.recipe.RecipeUtils.toIntegerArray;

import com.redhat.xmlrpc.binding.anno.ArrayPart;
import com.redhat.xmlrpc.binding.anno.DataIndex;
import com.redhat.xmlrpc.binding.anno.DataKey;
import com.redhat.xmlrpc.binding.anno.Ignored;
import com.redhat.xmlrpc.binding.anno.IndexRefs;
import com.redhat.xmlrpc.binding.anno.KeyRefs;
import com.redhat.xmlrpc.binding.anno.Request;
import com.redhat.xmlrpc.binding.anno.Response;
import com.redhat.xmlrpc.binding.anno.StructPart;
import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.ArrayRecipe;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.binding.recipe.StructRecipe;
import com.redhat.xmlrpc.binding.recipe.discovery.RecipeLoader;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ReflectionLoader
    implements RecipeLoader
{

    private static final Map<String, WeakReference<Set<Recipe<?>>>> ROOT_CACHE =
        new HashMap<String, WeakReference<Set<Recipe<?>>>>();

    public synchronized Collection<Recipe<?>> loadRecipes( final Class<?>... roots )
        throws BindException
    {
        final Set<Recipe<?>> recipes = new HashSet<Recipe<?>>();

        for ( final Class<?> root : roots )
        {
            Set<Recipe<?>> current;

            final String rootType = root.getName();
            final WeakReference<Set<Recipe<?>>> ref = ROOT_CACHE.get( rootType );
            if ( ref != null && ref.get() != null )
            {
                current = ref.get();
            }
            else
            {
                current = new HashSet<Recipe<?>>();

                if ( root.getAnnotation( Request.class ) != null || root.getAnnotation( Response.class ) != null )
                {
                    processArrayRecipe( root, current );
                }
                else
                {
                    throw new BindException(
                                             "Invalid message root. Class must be annotated with either @Request or @Response." );
                }

                ROOT_CACHE.put( rootType, new WeakReference<Set<Recipe<?>>>( current ) );
            }

            recipes.addAll( current );
        }

        return recipes;
    }

    protected void processArrayRecipe( final Class<?> type, final Set<Recipe<?>> recipes )
        throws BindException
    {
        final String typeName = type.getName();
        int[] ctorIndices = new int[0];

        for ( final Constructor<?> ctor : type.getDeclaredConstructors() )
        {
            final IndexRefs refs = ctor.getAnnotation( IndexRefs.class );
            if ( refs != null )
            {
                ctorIndices = refs.value();
                break;
            }
        }

        final ArrayRecipe recipe = new ArrayRecipe( typeName, typeName, toIntegerArray( ctorIndices ) );
        recipes.add( recipe );

        final SortedSet<Integer> taken = new TreeSet<Integer>();
        final Set<Field> noDecl = new HashSet<Field>();
        for ( final Field field : type.getDeclaredFields() )
        {
            if ( field.getAnnotation( Ignored.class ) == null )
            {
                final DataIndex di = field.getAnnotation( DataIndex.class );
                if ( di != null )
                {
                    if ( taken.contains( di.value() ) )
                    {
                        throw new BindException( "More than one field declares data-index: " + di.value() + " in: "
                            + typeName );
                    }

                    recipe.addFieldBinding( di.value(), field.getName(), typeOf( field, recipes ) );
                    taken.add( di.value() );
                }
                else
                {
                    noDecl.add( field );
                }
            }
        }

        int counter = 0;
        for ( final Field field : noDecl )
        {
            while ( taken.contains( counter ) )
            {
                counter++;
            }

            recipe.addFieldBinding( counter, field.getName(), typeOf( field, recipes ) );
            taken.add( counter );
        }
    }

    protected void processStructRecipe( final Class<?> type, final Set<Recipe<?>> recipes )
        throws BindException
    {
        final String typeName = type.getName();
        String[] ctorKeys = new String[0];

        for ( final Constructor<?> ctor : type.getDeclaredConstructors() )
        {
            final KeyRefs refs = ctor.getAnnotation( KeyRefs.class );
            if ( refs != null )
            {
                ctorKeys = refs.value();
                break;
            }
        }

        final StructRecipe recipe = new StructRecipe( typeName, typeName, ctorKeys );
        recipes.add( recipe );

        final Set<String> takenKeys = new HashSet<String>();
        final Set<Field> noDecl = new HashSet<Field>();
        for ( final Field field : type.getDeclaredFields() )
        {
            if ( field.getAnnotation( Ignored.class ) == null )
            {
                final DataKey dk = field.getAnnotation( DataKey.class );
                if ( dk != null )
                {
                    if ( takenKeys.contains( dk.value() ) )
                    {
                        throw new BindException( "More than one field declares data-key: " + dk.value() + " in: "
                            + typeName );
                    }

                    recipe.addFieldBinding( dk.value(), field.getName(), typeOf( field, recipes ) );
                    takenKeys.add( dk.value() );
                }
                else
                {
                    noDecl.add( field );
                }
            }
        }

        for ( final Field field : noDecl )
        {
            final String name = field.getName();
            if ( takenKeys.contains( name ) )
            {
                throw new BindException( "More than one field declares data-key: " + name + " in: " + typeName );
            }

            recipe.addFieldBinding( name, field.getName(), typeOf( field, recipes ) );
        }
    }

    protected String typeOf( final Field field, final Set<Recipe<?>> recipes )
        throws BindException
    {
        final Class<?> type = field.getType();
        String result = type.getName();
        if ( type.getAnnotation( ArrayPart.class ) != null )
        {
            processArrayRecipe( type, recipes );
            result = "recipe:" + result;
        }
        else if ( type.getAnnotation( StructPart.class ) != null )
        {
            processStructRecipe( type, recipes );
            result = "recipe:" + result;
        }

        return result;
    }

}
