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

import static com.redhat.xmlrpc.binding.anno.AnnotationUtils.isMessage;
import static com.redhat.xmlrpc.binding.recipe.RecipeUtils.toIntegerArray;

import com.redhat.xmlrpc.binding.anno.ArrayPart;
import com.redhat.xmlrpc.binding.anno.DataIndex;
import com.redhat.xmlrpc.binding.anno.DataKey;
import com.redhat.xmlrpc.binding.anno.Ignored;
import com.redhat.xmlrpc.binding.anno.IndexRefs;
import com.redhat.xmlrpc.binding.anno.KeyRefs;
import com.redhat.xmlrpc.binding.anno.Raw;
import com.redhat.xmlrpc.binding.anno.StructPart;
import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.ArrayRecipe;
import com.redhat.xmlrpc.binding.recipe.FieldBinding;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.binding.recipe.StructRecipe;
import com.redhat.xmlrpc.binding.recipe.discovery.RecipeLoader;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ReflectionLoader
    implements RecipeLoader
{

    private static final Map<String, WeakReference<Map<Class<?>, Recipe<?>>>> ROOT_CACHE =
        new HashMap<String, WeakReference<Map<Class<?>, Recipe<?>>>>();

    public synchronized Map<Class<?>, Recipe<?>> loadRecipes( final Class<?>... roots )
        throws BindException
    {
        final Map<Class<?>, Recipe<?>> recipes = new HashMap<Class<?>, Recipe<?>>();

        for ( final Class<?> root : roots )
        {
            Map<Class<?>, Recipe<?>> current;

            final String rootType = root.getName();
            final WeakReference<Map<Class<?>, Recipe<?>>> ref = ROOT_CACHE.get( rootType );
            if ( ref != null && ref.get() != null )
            {
                current = ref.get();
            }
            else
            {
                current = new HashMap<Class<?>, Recipe<?>>();

                if ( isMessage( root ) )
                {
                    processArrayRecipe( root, current );
                }
                else
                {
                    throw new BindException(
                                             "Invalid message root. Class must be annotated with either @Request or @Response." );
                }

                ROOT_CACHE.put( rootType, new WeakReference<Map<Class<?>, Recipe<?>>>( current ) );
            }

            recipes.putAll( current );
        }

        return recipes;
    }

    protected ArrayRecipe processArrayRecipe( final Class<?> type, final Map<Class<?>, Recipe<?>> recipes )
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

        final ArrayRecipe recipe = new ArrayRecipe( type, toIntegerArray( ctorIndices ) );
        recipes.put( type, recipe );

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

                    if ( Modifier.isTransient( field.getModifiers() ) )
                    {
                        throw new BindException( "Fields annotated with @DataIndex cannot be marked as transient!" );
                    }
                    else
                    {
                        addFieldBinding( recipe, di.value(), field, ctorIndices, recipes );
                        taken.add( di.value() );
                    }
                }
                else
                {
                    noDecl.add( field );
                }
            }
        }

        // FIXME: Add logged warnings!!
        // TODO: Validate @IndexRefs against acutal discovered @DataIndex annotation values.
        // TODO: Warn about @DataKey in ArrayRecipe types...
        // TODO: Warn about unbound fields, or types with NO bound fields.
        // TODO: Validate ctor keys against field bindings.
        //        int counter = 0;
        //        for ( final Field field : noDecl )
        //        {
        //            while ( taken.contains( counter ) )
        //            {
        //                counter++;
        //            }
        //
        //            addFieldBinding( recipe, counter, field.getName(), field.getType(), recipes );
        //            taken.add( counter );
        //        }

        return recipe;
    }

    protected void addFieldBinding( final ArrayRecipe recipe, final int index, final Field field,
                                    final int[] ctorIndices, final Map<Class<?>, Recipe<?>> recipes )
        throws BindException
    {
        if ( Modifier.isFinal( field.getModifiers() ) )
        {
            boolean found = false;
            for ( final int i : ctorIndices )
            {
                if ( index == i )
                {
                    found = true;
                    break;
                }
            }

            if ( !found )
            {
                throw new BindException(
                                         "Fields annotated with @DataIndex cannot be marked as final unless they're included in the @IndexRefs constructor annotation!" );
            }
        }

        final Class<?> type = field.getType();
        final String name = field.getName();

        if ( field.getAnnotation( Raw.class ) != null )
        {
            recipe.addFieldBinding( index, new FieldBinding( name, type, true ) );
        }
        else
        {
            processBindingTarget( type, recipes );
            recipe.addFieldBinding( index, new FieldBinding( name, type ) );
        }
    }

    protected void addFieldBinding( final StructRecipe recipe, final String key, final Field field,
                                    final String[] ctorKeys, final Map<Class<?>, Recipe<?>> recipes )
        throws BindException
    {
        if ( Modifier.isTransient( field.getModifiers() ) )
        {
            throw new BindException( "Fields annotated with @DataKey cannot be marked as transient!" );
        }
        else if ( Modifier.isFinal( field.getModifiers() ) )
        {
            boolean found = false;
            for ( final String k : ctorKeys )
            {
                if ( key.equals( k ) )
                {
                    found = true;
                    break;
                }
            }

            if ( !found )
            {
                throw new BindException(
                                         "Fields annotated with @DataKey cannot be marked as final unless they're included in the @KeyRefs constructor annotation!" );
            }
        }

        final Class<?> type = field.getType();
        final String name = field.getName();

        if ( field.getAnnotation( Raw.class ) != null )
        {
            recipe.addFieldBinding( key, new FieldBinding( name, type, true ) );
        }
        else
        {
            processBindingTarget( type, recipes );
            recipe.addFieldBinding( key, new FieldBinding( name, type ) );
        }
    }

    protected Recipe<?> processBindingTarget( final Class<?> type, final Map<Class<?>, Recipe<?>> recipes )
        throws BindException
    {
        if ( type.getAnnotation( ArrayPart.class ) != null )
        {
            return processArrayRecipe( type, recipes );
        }
        else if ( type.getAnnotation( StructPart.class ) != null )
        {
            return processStructRecipe( type, recipes );
        }

        return null;
    }

    protected StructRecipe processStructRecipe( final Class<?> type, final Map<Class<?>, Recipe<?>> recipes )
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

        final StructRecipe recipe = new StructRecipe( type, ctorKeys );
        recipes.put( type, recipe );

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

                    addFieldBinding( recipe, dk.value(), field, ctorKeys, recipes );
                    takenKeys.add( dk.value() );
                }
                else
                {
                    noDecl.add( field );
                }
            }
        }

        // FIXME: Add logged warnings!!
        // TODO: Validate @KeyRefs against acutal discovered @DataKey annotation values.
        // TODO: Warn about @DataIndex in StructRecipe types...
        // TODO: Warn about implicitly fields
        // TODO: Warn about types having NO explicitly bound fields.
        // TODO: Validate ctor keys against field bindings.
        for ( final Field field : noDecl )
        {
            final String name = field.getName();
            if ( takenKeys.contains( name ) )
            {
                throw new BindException( "More than one field declares data-key: " + name + " in: " + typeName );
            }

            if ( !Modifier.isTransient( field.getModifiers() ) )
            {
                addFieldBinding( recipe, name, field, ctorKeys, recipes );
            }
        }

        return recipe;
    }

}
