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

import java.util.Collections;
import java.util.Map;

public final class StructClassRecipe
    implements ClassRecipe
{

    private final String className;

    private final String[] ctorKeys;

    private final Map<String, FieldMapping> fields;

    public StructClassRecipe( final String className, final Map<String, FieldMapping> fields, final String[] ctorKeys )
    {
        this.className = className;
        this.fields = Collections.unmodifiableMap( fields );
        this.ctorKeys = ctorKeys;
    }

    public String getClassName()
    {
        return className;
    }

    public FieldMapping[] getFieldMappings()
    {
        return fields.values().toArray( new FieldMapping[] {} );
    }

    public Map<String, FieldMapping> getFieldsByKey()
    {
        return fields;
    }

    public String[] getConstructorKeys()
    {
        return ctorKeys;
    }

}
