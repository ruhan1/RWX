/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
 */

package org.commonjava.rwx.binding.mapping;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Map;

// FIXME: Move to Class<?> type references, and get rid of name in favor of type. We don't need aliases for classes.
public interface Mapping<T>
    extends Externalizable, Serializable
{

    T[] getConstructorKeys();

    Map<T, FieldBinding> getFieldBindings();

    Class<?> getObjectType();

    FieldBinding getFieldBinding( T key );

}
