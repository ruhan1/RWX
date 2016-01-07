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
package org.commonjava.rwx.binding.internal.xbr.helper;

import org.apache.xbean.recipe.ArrayRecipe;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.spi.Binder;

import java.lang.reflect.Field;

public class ArrayBinder
    extends CollectionBinder
    implements Binder
{

    public ArrayBinder( final Binder parent, final Class<?> valueType, final Field field,
                        final XBRBindingContext context )
    {
        super( parent, null, valueType, field, context );
    }

    @Override
    protected Object create()
    {
        final ArrayRecipe recipe = new ArrayRecipe( getType() );
        recipe.addAll( getValues() );
        return recipe.create();
    }

}
