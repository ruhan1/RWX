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
import java.io.Serializable;
import java.util.Map;

// FIXME: Move to Class<?> type references, and get rid of name in favor of type. We don't need aliases for classes.
public interface Recipe<T>
    extends Externalizable, Serializable
{

    T[] getConstructorKeys();

    Map<T, FieldBinding> getFieldBindings();

    String getName();

    String getObjectType();

    FieldBinding getFieldBinding( T key );

}
