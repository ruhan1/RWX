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

import java.util.ArrayList;
import java.util.List;

public class ArrayClassRecipeBuilder
{

    private String className;

    private final List<FieldMapping> fields = new ArrayList<FieldMapping>();

    private int[] ctorIndices;

    public ArrayClassRecipeBuilder withClassName( final String className )
    {
        this.className = className;
        return this;
    }

    public ArrayClassRecipeBuilder withFieldInOrder( final String className, final String fieldName )
    {
        return withFieldInOrder( className, fieldName, null );
    }

    public ArrayClassRecipeBuilder withFieldInOrder( final String fieldName, final String fieldType,
                                                     final String converterType )
    {
        fields.add( new FieldMapping( fields.size(), fieldName, fieldType, converterType ) );

        return this;
    }

    public ArrayClassRecipeBuilder withConstructorIndices( final int... indices )
    {
        ctorIndices = indices;
        return this;
    }

    private void validate()
        throws BindException
    {
        if ( className == null || className.trim().length() < 1 )
        {
            throw new BindException( "You must specify a class-name for the recipe!" );
        }

        final List<Integer> unmatched = new ArrayList<Integer>();

        for ( final int key : ctorIndices )
        {
            if ( key < 0 )
            {
                unmatched.add( key );
            }
            else if ( fields.size() <= key )
            {
                unmatched.add( key );
            }
        }

        if ( !unmatched.isEmpty() )
        {
            final StringBuilder sb = new StringBuilder();
            for ( final Integer i : unmatched )
            {
                sb.append( i ).append( ", " );
            }
            sb.setLength( sb.length() - 2 );

            throw new BindException( "The following constructor indices were unmatched in the field-mapping array: "
                + sb.toString() );
        }
    }

    public ClassRecipe build()
        throws BindException
    {
        validate();
        return new ArrayClassRecipe( className, fields.toArray( new FieldMapping[] {} ), ctorIndices );
    }

}
