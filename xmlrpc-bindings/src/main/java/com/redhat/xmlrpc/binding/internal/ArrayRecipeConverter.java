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
import com.redhat.xmlrpc.binding.recipe.ArrayClassRecipe;
import com.redhat.xmlrpc.binding.recipe.FieldMapping;
import com.redhat.xmlrpc.raw.model.XmlRpcArray;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayRecipeConverter
    extends AbstractRecipeConverter<ArrayClassRecipe>
{

    private List<FieldMapping> fields;

    private Map<String, Object> fieldValues;

    private Object[] ctorParams;

    private Class<?>[] ctorParamTypes;

    public ArrayRecipeConverter( final ArrayClassRecipe recipe )
    {
        super( recipe );
    }

    @Override
    public XmlRpcValue<?> render( final Object src, final BinderyContext context )
        throws BindException
    {
        // TODO Implement ArrayRecipeConverter.render
        throw new UnsupportedOperationException( "Not Implemented." );
    }

    @Override
    public Object parse( final XmlRpcValue<?> src, final BinderyContext context )
        throws BindException
    {
        final XmlRpcArray array = (XmlRpcArray) src;
        final ArrayClassRecipe recipe = getRecipe();

        Class<?> type;
        try
        {
            type = Class.forName( recipe.getClassName() );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new BindException( "Cannot find class for recipe: " + recipe.getClassName(), e );
        }

        fields = new ArrayList<FieldMapping>();
        for ( final FieldMapping mapping : recipe.getFieldMappings() )
        {
            fields.add( mapping );
        }

        loadFieldValues( array, context );
        loadCtorParams( context );

        final Object result = context.construct( type, ctorParams, ctorParamTypes );

        assignFields( result, type, context );

        return result;
    }

    private void assignFields( final Object result, final Class<?> type, final BinderyContext context )
        throws BindException
    {
        for ( final FieldMapping mapping : fields )
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
        final ArrayClassRecipe recipe = getRecipe();

        final int ctorLen = recipe.getConstructorIndices() == null ? 0 : recipe.getConstructorIndices().length;
        final Object[] ctorParams = new Object[ctorLen];
        final Class<?>[] ctorTypes = new Class<?>[ctorLen];
        if ( ctorLen > 0 )
        {
            int i = 0;
            for ( final int idx : recipe.getConstructorIndices() )
            {
                final FieldMapping mapping = fields.remove( idx );
                ctorParams[i] = fieldValues.get( mapping.getFieldName() );
                ctorTypes[i] = context.loadClass( mapping.getFieldType() );
                i++;
            }
        }
    }

    private Map<String, Object> loadFieldValues( final XmlRpcArray array, final BinderyContext context )
        throws BindException
    {
        final Map<String, Object> values = new HashMap<String, Object>();

        final FieldMapping[] fieldMappings = getRecipe().getFieldMappings();

        final int i = 0;
        for ( final XmlRpcValue<?> value : array )
        {
            final FieldMapping mapping = fieldMappings[i];

            final String name = mapping.getFieldName();

            final Class<?> fieldType = context.loadClass( mapping.getFieldType() );
            if ( mapping.getFieldConverterType() == null )
            {
                values.put( name, context.parse( value, fieldType ) );
            }
            else
            {
                values.put( name, context.parseWithConverter( value, fieldType, mapping.getFieldConverterType() ) );
            }
        }

        for ( final FieldMapping mapping : getRecipe().getFieldMappings() )
        {
            final String name = mapping.getFieldName();
            final XmlRpcValue<?> value = array.get( mapping.getIndex() );

            final Class<?> fieldType = context.loadClass( mapping.getFieldType() );
            if ( mapping.getFieldConverterType() == null )
            {
                values.put( name, context.parse( value, fieldType ) );
            }
            else
            {
                values.put( name, context.parseWithConverter( value, fieldType, mapping.getFieldConverterType() ) );
            }
        }

        return values;
    }

}
