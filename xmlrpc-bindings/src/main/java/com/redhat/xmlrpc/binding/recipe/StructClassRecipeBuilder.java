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

package com.redhat.xmlrpc.binding.recipe;

import com.redhat.xmlrpc.binding.error.BindException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StructClassRecipeBuilder
{

    private String className;

    private final Map<String, FieldMapping> fields = new HashMap<String, FieldMapping>();

    private String[] ctorKeys;

    public StructClassRecipeBuilder withClassName( final String className )
    {
        this.className = className;
        return this;
    }

    public StructClassRecipeBuilder withField( final String key, final String className, final String fieldName )
    {
        return withField( key, className, fieldName, null );
    }

    public StructClassRecipeBuilder withField( final String key, final String fieldName, final String fieldType,
                                         final String converterType )
    {
        fields.put( key, new FieldMapping( key, fieldName, fieldType, converterType ) );

        return this;
    }

    public StructClassRecipeBuilder withConstructorKeys( final String... keys )
    {
        ctorKeys = keys;
        return this;
    }

    private void validate()
        throws BindException
    {
        if ( className == null || className.trim().length() < 1 )
        {
            throw new BindException( "You must specify a class-name for the recipe!" );
        }

        final Set<String> unmatched = new HashSet<String>();

        for ( final String key : ctorKeys )
        {
            if ( !fields.containsKey( key ) )
            {
                unmatched.add( key );
            }
        }
    }

    public ClassRecipe build()
        throws BindException
    {
        validate();
        return new StructClassRecipe( className, fields, ctorKeys );
    }

}
