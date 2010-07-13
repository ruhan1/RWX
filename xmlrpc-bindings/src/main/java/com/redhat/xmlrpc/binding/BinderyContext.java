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

package com.redhat.xmlrpc.binding;

import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.internal.DefaultValueConverter;
import com.redhat.xmlrpc.binding.recipe.ClassRecipe;
import com.redhat.xmlrpc.binding.spi.ValueConverter;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class BinderyContext
{

    protected static final DefaultValueConverter DEFAULT_CONVERTER = new DefaultValueConverter();

    private static final Map<String, WeakReference<ValueConverter>> CONVERTERS =
        new HashMap<String, WeakReference<ValueConverter>>();

    private final Map<String, ClassRecipe> recipes;

    public BinderyContext( final Map<String, ClassRecipe> recipes )
    {
        this.recipes = recipes;
    }

    @SuppressWarnings( "unchecked" )
    public <T> T parseWithConverter( final XmlRpcValue<?> value, final Class<T> fieldType, final String converterType )
        throws BindException
    {
        final ValueConverter converter = converterFor( converterType );
        if ( converter == null )
        {
            return (T) DEFAULT_CONVERTER.parse( value, this );
        }
        else
        {
            return (T) converter.parse( value, this );
        }
    }

    public <T> T parse( final XmlRpcValue<?> rpcValue, final Class<T> type )
        throws BindException
    {
        final ClassRecipe recipe = recipes.get( type.getName() );
        if ( recipe != null )
        {

        }

        return null;
    }

    public Class<?> loadClass( final String className )
        throws BindException
    {
        try
        {
            return Class.forName( className );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new BindException( "Cannot find class for recipe: " + className, e );
        }
    }

    public Field fieldFor( final Class<?> type, final String fieldName )
        throws BindException
    {
        try
        {
            return type.getDeclaredField( fieldName );
        }
        catch ( final SecurityException e )
        {
            throw new BindException( "Failed to retrieve field: " + fieldName + " from: " + type.getName()
                + ". Reason: " + e.getMessage(), e );
        }
        catch ( final NoSuchFieldException e )
        {
            throw new BindException( "Failed to retrieve field: " + fieldName + " from: " + type.getName()
                + ". Reason: " + e.getMessage(), e );
        }
    }

    public <T> T construct( final Class<T> type, final Object[] params, final Class<?>[] types )
        throws BindException
    {
        try
        {
            final Constructor<T> ctor = type.getDeclaredConstructor( types );

            if ( !ctor.isAccessible() )
            {
                ctor.setAccessible( true );
            }

            return ctor.newInstance( params );
        }
        catch ( final InstantiationException e )
        {
            throw new BindException( "Failed to instantiate: " + type.getName() + ". Reason: " + e.getMessage(), e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new BindException( "Failed to instantiate: " + type.getName() + ". Reason: " + e.getMessage(), e );
        }
        catch ( final InvocationTargetException e )
        {
            e.initCause( e.getTargetException() );

            throw new BindException( "Error in constructor call for: " + type.getName() + ". Reason: "
                + e.getTargetException().getMessage(), e );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new BindException( "Cannot find appropriate constructor for: " + type.getName() + ". Reason: "
                + e.getMessage(), e );
        }
    }

    public static ValueConverter converterFor( final String fieldConverterType )
        throws BindException
    {
        final WeakReference<ValueConverter> ref = CONVERTERS.get( fieldConverterType );

        ValueConverter result = null;
        if ( ref != null )
        {
            result = ref.get();
        }

        if ( ref == null )
        {
            try
            {
                result = (ValueConverter) Class.forName( fieldConverterType ).newInstance();
                CONVERTERS.put( fieldConverterType, new WeakReference<ValueConverter>( result ) );
            }
            catch ( final InstantiationException e )
            {
                throw new BindException( "Failed to load value converter: " + fieldConverterType + ". Reason: "
                    + e.getMessage(), e );
            }
            catch ( final IllegalAccessException e )
            {
                throw new BindException( "Failed to load value converter: " + fieldConverterType + ". Reason: "
                    + e.getMessage(), e );
            }
            catch ( final ClassNotFoundException e )
            {
                throw new BindException( "Failed to load value converter: " + fieldConverterType + ". Reason: "
                    + e.getMessage(), e );
            }
        }

        return result;
    }

}
