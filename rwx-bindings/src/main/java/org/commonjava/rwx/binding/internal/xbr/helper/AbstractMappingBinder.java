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

package org.commonjava.rwx.binding.internal.xbr.helper;

import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.Binder;

public abstract class AbstractMappingBinder<T extends Mapping<?>>
    extends AbstractBinder
    implements Binder
{

    private final T mapping;

    protected AbstractMappingBinder( final Binder parent, final Class<?> type, final T mapping,
                                     final XBRBindingContext context )
    {
        super( parent, type, context );
        if ( !mapping.getObjectType().isAssignableFrom( type ) )
        {
            throw new IllegalArgumentException( "Object-type from mapping: " + mapping.getObjectType().getName()
                + " is not assignable from given type: " + type.getName() );
        }

        this.mapping = mapping;
    }

    protected final T getMapping()
    {
        return mapping;
    }

}
