/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
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

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.reflect.ReflectionMapper;
import org.commonjava.rwx.binding.internal.reflect.ReflectionUnbindery;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.composed.CompositionBindery;
import org.jdom.output.XMLOutputter;


import java.util.Map;

public class XBRCompositionBindery
    extends CompositionBindery
{

    public static XBRCompositionBindery binderyFor( final Class<?>... roots )
        throws BindException
    {
        return binderyFor( null, roots );
    }

    public static XBRCompositionBindery binderyFor( final XMLOutputter outputter, final Class<?>... roots )
        throws BindException
    {
        final Map<Class<?>, Mapping<?>> recipes = new ReflectionMapper().loadRecipes( roots );
        return new XBRCompositionBindery( recipes, outputter );
    }

    public XBRCompositionBindery( final Map<Class<?>, Mapping<?>> recipes )
        throws BindException
    {
        this( recipes, null );
    }

    public XBRCompositionBindery( final Map<Class<?>, Mapping<?>> recipes, final XMLOutputter outputter )
        throws BindException
    {
        super( new ReflectionUnbindery( recipes, outputter ), new XBeanRenderingBindery( recipes ) );
    }

}
