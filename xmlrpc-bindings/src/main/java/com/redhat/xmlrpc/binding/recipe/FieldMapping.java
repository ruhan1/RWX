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

public class FieldMapping
{

    private final String key;

    private final String fieldName;

    private final String fieldType;

    private final String fieldConverterType;

    private final int index;

    public FieldMapping( final String key, final String fieldName, final String fieldType,
                         final String fieldConverterType )
    {
        this.key = key;
        index = -1;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldConverterType = fieldConverterType;
    }

    public FieldMapping( final int index, final String fieldName, final String fieldType,
                         final String fieldConverterType )
    {
        this.index = index;
        key = null;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldConverterType = fieldConverterType;
    }

    public int getIndex()
    {
        return index;
    }

    public String getKey()
    {
        return key;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public String getFieldConverterType()
    {
        return fieldConverterType;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ( ( key == null ) ? 0 : key.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final FieldMapping other = (FieldMapping) obj;
        if ( index != other.index )
        {
            return false;
        }
        if ( key == null )
        {
            if ( other.key != null )
            {
                return false;
            }
        }
        else if ( !key.equals( other.key ) )
        {
            return false;
        }
        return true;
    }

}
