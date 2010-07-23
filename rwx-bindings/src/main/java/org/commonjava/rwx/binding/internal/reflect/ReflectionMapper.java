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

package org.commonjava.rwx.binding.internal.reflect;

import static org.commonjava.rwx.binding.anno.AnnotationUtils.isMessage;
import static org.commonjava.rwx.binding.mapping.MappingUtils.toIntegerArray;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.Converter;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.binding.anno.Ignored;
import org.commonjava.rwx.binding.anno.IndexRefs;
import org.commonjava.rwx.binding.anno.KeyRefs;
import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx.binding.convert.ValueConverter;
import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.StructMapping;
import org.commonjava.rwx.binding.mapping.discovery.Mapper;

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

public class ReflectionMapper
    implements Mapper
{

    private static final Map<String, WeakReference<Map<Class<?>, Mapping<?>>>> ROOT_CACHE =
        new HashMap<String, WeakReference<Map<Class<?>, Mapping<?>>>>();

    public synchronized Map<Class<?>, Mapping<?>> loadRecipes( final Class<?>... roots )
        throws BindException
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        for ( final Class<?> root : roots )
        {
            Map<Class<?>, Mapping<?>> current;

            final String rootType = root.getName();
            final WeakReference<Map<Class<?>, Mapping<?>>> ref = ROOT_CACHE.get( rootType );
            if ( ref != null && ref.get() != null )
            {
                current = ref.get();
            }
            else
            {
                current = new HashMap<Class<?>, Mapping<?>>();

                if ( isMessage( root ) )
                {
                    processArrayRecipe( root, current );
                }
                else
                {
                    throw new BindException(
                                             "Invalid message root. Class must be annotated with either @Request or @Response." );
                }

                ROOT_CACHE.put( rootType, new WeakReference<Map<Class<?>, Mapping<?>>>( current ) );
            }

            recipes.putAll( current );
        }

        return recipes;
    }

    protected ArrayMapping processArrayRecipe( final Class<?> type, final Map<Class<?>, Mapping<?>> recipes )
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

        final ArrayMapping recipe = new ArrayMapping( type, toIntegerArray( ctorIndices ) );
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

    protected void addFieldBinding( final ArrayMapping recipe, final int index, final Field field,
                                    final int[] ctorIndices, final Map<Class<?>, Mapping<?>> recipes )
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

        final Converter converter = field.getAnnotation( Converter.class );
        if ( converter != null )
        {
            loadSupplementalRecipes( converter, recipes, recipe, name );
            recipe.addFieldBinding( index, new FieldBinding( name, type, converter.value() ) );
        }
        else
        {
            final Contains contains = field.getAnnotation( Contains.class );
            if ( contains != null )
            {
                processBindingTarget( contains.value(), recipes );
            }
            else
            {
                processBindingTarget( type, recipes );
            }
            recipe.addFieldBinding( index, new FieldBinding( name, type ) );
        }
    }

    protected void addFieldBinding( final StructMapping recipe, final String key, final Field field,
                                    final String[] ctorKeys, final Map<Class<?>, Mapping<?>> recipes )
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

        final Converter converter = field.getAnnotation( Converter.class );
        if ( converter != null )
        {
            loadSupplementalRecipes( converter, recipes, recipe, name );
            recipe.addFieldBinding( key, new FieldBinding( name, type, converter.value() ) );
        }
        else
        {
            final Contains contains = field.getAnnotation( Contains.class );
            if ( contains != null )
            {
                processBindingTarget( contains.value(), recipes );
            }
            else
            {
                processBindingTarget( type, recipes );
            }
            recipe.addFieldBinding( key, new FieldBinding( name, type ) );
        }
    }

    protected void loadSupplementalRecipes( final Converter converter, final Map<Class<?>, Mapping<?>> recipes,
                                            final Mapping<?> recipe, final String fieldName )
        throws BindException
    {
        try
        {
            final ValueConverter vc = converter.value().newInstance();
            final Map<Class<?>, Mapping<?>> supplementalRecipes = vc.getSupplementalRecipes( this );
            if ( supplementalRecipes != null && !supplementalRecipes.isEmpty() )
            {
                recipes.putAll( supplementalRecipes );
            }
        }
        catch ( final InstantiationException e )
        {
            throw new BindException( "Cannot create ValueConverter: " + converter.value().getName() + "\nField: "
                + fieldName + "\nClass: " + recipe.getObjectType().getName() + "\nError: " + e.getMessage(), e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new BindException( "Cannot create ValueConverter: " + converter.value().getName() + "\nField: "
                + fieldName + "\nClass: " + recipe.getObjectType().getName() + "\nError: " + e.getMessage(), e );
        }
    }

    protected Mapping<?> processBindingTarget( final Class<?> type, final Map<Class<?>, Mapping<?>> recipes )
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

    protected StructMapping processStructRecipe( final Class<?> type, final Map<Class<?>, Mapping<?>> recipes )
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

        final StructMapping recipe = new StructMapping( type, ctorKeys );
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
