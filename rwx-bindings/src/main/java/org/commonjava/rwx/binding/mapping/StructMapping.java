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

public class StructMapping
    extends AbstractMapping<String>
{

    public StructMapping( final Class<?> objectType, final String... constructorKeys )
    {
        super( objectType, constructorKeys );
    }

    public final StructMapping addFieldBinding( final String key, final FieldBinding binding )
    {
        putBinding( key, binding );
        return this;
    }

}
