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

public class ArrayClassRecipe
    implements ClassRecipe
{

    private final String className;

    private final FieldMapping[] mappings;

    private final int[] ctorIndices;

    public ArrayClassRecipe( final String className, final FieldMapping[] mappings, final int[] ctorIndices )
    {
        this.className = className;
        this.mappings = mappings;
        this.ctorIndices = ctorIndices;
    }

    @Override
    public String getClassName()
    {
        return className;
    }

    @Override
    public FieldMapping[] getFieldMappings()
    {
        return mappings;
    }

    public int[] getConstructorIndices()
    {
        return ctorIndices;
    }

}
