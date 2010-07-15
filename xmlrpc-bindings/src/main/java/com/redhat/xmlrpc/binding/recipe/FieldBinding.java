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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

// FIXME: Move to Class<?> type refs, and forget marking for external recipe refs.
// FIXME: Incorporate ValueConverters.
public class FieldBinding
    implements Externalizable, Serializable
{

    private static final long serialVersionUID = 1L;

    private String fieldName;

    private String fieldType;

    public FieldBinding( final String fieldName, final String fieldType )
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public boolean isRecipeReference()
    {
        return fieldType.startsWith( "recipe:" );
    }

    public String getFieldType()
    {
        return fieldType;
    }

    @Override
    public void readExternal( final ObjectInput in )
        throws IOException, ClassNotFoundException
    {
        fieldName = (String) in.readObject();
        fieldType = (String) in.readObject();
    }

    @Override
    public void writeExternal( final ObjectOutput out )
        throws IOException
    {
        out.writeObject( fieldName );
        out.writeObject( fieldType );
    }

}
