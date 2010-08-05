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

package org.commonjava.rwx.binding.internal.xbr;

import org.apache.xbean.recipe.ConstructionException;
import org.apache.xbean.recipe.ObjectRecipe;
import org.apache.xbean.recipe.Option;
import org.commonjava.rwx.binding.anno.BindVia;
import org.commonjava.rwx.binding.anno.UnbindVia;
import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.binding.spi.value.ValueBinder;
import org.commonjava.rwx.binding.spi.value.ValueUnbinder;

public final class XBRBinderInstantiator
{

    private static final Class<?>[] BIND_VIA_ARG_TYPES = { Binder.class, Class.class, BindingContext.class };

    private static final String PARENT_KEY = "parent";

    private static final String TYPE_KEY = "type";

    private static final String CONTEXT_KEY = "context";

    private static final String[] BIND_VIA_ARG_NAMES = { PARENT_KEY, TYPE_KEY, CONTEXT_KEY };

    private XBRBinderInstantiator()
    {
    }

    public static ValueBinder newValueBinder( final BindVia bindVia, final Binder parent, final Class<?> type,
                                              final BindingContext context )
        throws BindException
    {
        try
        {
            final ObjectRecipe viaRecipe = buildRecipe( bindVia.value(), parent, type, context );

            return (ValueBinder) viaRecipe.create();
        }
        catch ( final ConstructionException e )
        {
            throw new BindException( "Cannot create ValueBinder from @BindVia( " + bindVia.value().getName() + "): "
                + e.getMessage(), e );
        }
    }

    public static ValueUnbinder newValueUnbinder( final UnbindVia unbindVia, final Binder parent, final Class<?> type,
                                                  final BindingContext context )
        throws BindException
    {
        try
        {
            final ObjectRecipe viaRecipe = buildRecipe( unbindVia.value(), parent, type, context );

            return (ValueUnbinder) viaRecipe.create();
        }
        catch ( final ConstructionException e )
        {
            throw new BindException( "Cannot create ValueBinder from @BindVia( " + unbindVia.value().getName() + "): "
                + e.getMessage(), e );
        }
    }

    private static ObjectRecipe buildRecipe( final Class<?> binderType, final Binder parent, final Class<?> boundType,
                                             final BindingContext context )
    {
        final ObjectRecipe viaRecipe = new ObjectRecipe( binderType );

        try
        {
            // test whether this ctor type is used...
            binderType.getDeclaredConstructor( BIND_VIA_ARG_TYPES );

            viaRecipe.setConstructorArgNames( BIND_VIA_ARG_NAMES );
            viaRecipe.setConstructorArgTypes( BIND_VIA_ARG_TYPES );
        }
        catch ( final NoSuchMethodException e )
        {
            // default to an empty constructor...
            viaRecipe.setConstructorArgNames( new String[0] );
            viaRecipe.setConstructorArgTypes( new Class<?>[0] );
        }

        viaRecipe.allow( Option.IGNORE_MISSING_PROPERTIES );

        viaRecipe.setProperty( PARENT_KEY, parent );
        viaRecipe.setProperty( TYPE_KEY, boundType );
        viaRecipe.setProperty( CONTEXT_KEY, context );

        return viaRecipe;
    }

}
