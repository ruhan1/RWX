/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.rwx.binding.internal.xbr;

import org.apache.xbean.recipe.ConstructionException;
import org.apache.xbean.recipe.ObjectRecipe;
import org.apache.xbean.recipe.Option;
import org.commonjava.rwx.binding.anno.Converter;
import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.binding.spi.value.ValueBinder;

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

    public static ValueBinder newValueBinder( final Converter bindVia, final Binder parent, final Class<?> type,
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
            throw new BindException( "Cannot create ValueBinder from @Converter( " + bindVia.value().getName() + "): "
                + e.getMessage(), e );
        }
    }

    public static ValueBinder newValueUnbinder( final Converter bindVia )
        throws BindException
    {
        try
        {
            final ObjectRecipe viaRecipe = buildRecipe( bindVia.value(), null, null, null );

            return (ValueBinder) viaRecipe.create();
        }
        catch ( final ConstructionException e )
        {
            throw new BindException( "Cannot create ValueBinder from @BindVia( " + bindVia.value().getName() + "): "
                + e.getMessage(), e );
        }
    }

    private static ObjectRecipe buildRecipe( final Class<?> binderType, final Binder parent, final Class<?> boundType,
                                             final BindingContext context )
        throws BindException
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
            throw new BindException( "Cannot create ValueBinder from @BindVia( " + binderType.getName() + "): "
                + e.getMessage(), e );
        }

        viaRecipe.allow( Option.IGNORE_MISSING_PROPERTIES );

        viaRecipe.setProperty( PARENT_KEY, parent );
        viaRecipe.setProperty( TYPE_KEY, boundType );
        viaRecipe.setProperty( CONTEXT_KEY, context );

        return viaRecipe;
    }

}
