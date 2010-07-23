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

package org.commonjava.rwx.binding.internal.xbr;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.reflect.ReflectionMapper;
import org.commonjava.rwx.binding.internal.reflect.ReflectionUnbindery;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.composed.CompositionBindery;
import org.jdom.output.XMLOutputter;


import java.util.Map;

public final class XBRCompositionBindery
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
