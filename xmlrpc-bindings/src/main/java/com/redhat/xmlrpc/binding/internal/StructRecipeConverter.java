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

package com.redhat.xmlrpc.binding.internal;

import com.redhat.xmlrpc.binding.BinderyContext;
import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.FieldMapping;
import com.redhat.xmlrpc.binding.recipe.StructClassRecipe;
import com.redhat.xmlrpc.raw.model.XmlRpcStruct;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StructRecipeConverter
    extends AbstractRecipeConverter<StructClassRecipe>
{

    private Map<String, FieldMapping> fieldsByKey;

    private Map<String, Object> fieldValues;

    private Object[] ctorParams;

    private Class<?>[] ctorParamTypes;

    public StructRecipeConverter( final StructClassRecipe recipe )
    {
        super( recipe );
    }

    @Override
    public XmlRpcValue<?> render( final Object src, final BinderyContext context )
        throws BindException
    {
        // TODO Implement ValueConverter.render
        throw new UnsupportedOperationException( "Not Implemented." );
    }

    @Override
    public Object parse( final XmlRpcValue<?> src, final BinderyContext context )
        throws BindException
    {
        final XmlRpcStruct struct = (XmlRpcStruct) src;
        final StructClassRecipe recipe = getRecipe();

        Class<?> type;
        try
        {
            type = Class.forName( recipe.getClassName() );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new BindException( "Cannot find class for recipe: " + recipe.getClassName(), e );
        }

        fieldsByKey = recipe.getFieldsByKey();

        loadFieldValues( struct, context );
        loadCtorParams( context );

        final Object result = context.construct( type, ctorParams, ctorParamTypes );

        assignFields( result, type, context );

        return result;
    }

    private void assignFields( final Object result, final Class<?> type, final BinderyContext context )
        throws BindException
    {
        for ( final FieldMapping mapping : fieldsByKey.values() )
        {
            final String key = mapping.getKey();
            final Object value = fieldValues.get( key );
            final String fieldName = mapping.getFieldName();

            final Field field = context.fieldFor( type, fieldName );
            if ( !field.isAccessible() )
            {
                field.setAccessible( true );
                try
                {
                    field.set( result, value );
                }
                catch ( final IllegalAccessException e )
                {
                    throw new BindException( "Failed to set value: " + value + " for field: " + fieldName + " in: "
                        + type.getName() + "\nReason: " + e.getMessage(), e );
                }
            }
        }
    }

    private void loadCtorParams( final BinderyContext context )
        throws BindException
    {
        final StructClassRecipe recipe = getRecipe();

        final int ctorLen = recipe.getConstructorKeys() == null ? 0 : recipe.getConstructorKeys().length;
        final Object[] ctorParams = new Object[ctorLen];
        final Class<?>[] ctorTypes = new Class<?>[ctorLen];
        if ( ctorLen > 0 )
        {
            int i = 0;
            for ( final String key : recipe.getConstructorKeys() )
            {
                ctorParams[i] = fieldValues.get( key );

                final FieldMapping mapping = fieldsByKey.remove( key );
                ctorTypes[i] = context.loadClass( mapping.getFieldType() );
                i++;
            }
        }
    }

    private Map<String, Object> loadFieldValues( final XmlRpcStruct struct, final BinderyContext context )
        throws BindException
    {
        final Map<String, Object> values = new HashMap<String, Object>();
        for ( final FieldMapping mapping : getRecipe().getFieldMappings() )
        {
            final String key = mapping.getKey();
            final XmlRpcValue<?> value = struct.get( key );

            final Class<?> fieldType = context.loadClass( mapping.getFieldType() );
            if ( mapping.getFieldConverterType() == null )
            {
                values.put( key, context.parse( value, fieldType ) );
            }
            else
            {
                values.put( key, context.parseWithConverter( value, fieldType, mapping.getFieldConverterType() ) );
            }
        }

        return values;
    }

}
