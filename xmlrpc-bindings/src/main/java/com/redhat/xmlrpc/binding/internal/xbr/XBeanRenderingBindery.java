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

package com.redhat.xmlrpc.binding.internal.xbr;

import static com.redhat.xmlrpc.binding.recipe.RecipeUtils.mapRecipesByClass;

import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.internal.ParsingBinderyDelegate;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.stax.StaxParser;
import com.redhat.xmlrpc.spi.XmlRpcGenerator;
import com.redhat.xmlrpc.spi.XmlRpcParser;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;

public class XBeanRenderingBindery
    extends ParsingBinderyDelegate
{

    private final Collection<Recipe<?>> recipes;

    private final Map<Class<?>, Recipe<?>> recipesByClass;

    public XBeanRenderingBindery( final Collection<Recipe<?>> recipes )
        throws BindException
    {
        this.recipes = recipes;
        recipesByClass = mapRecipesByClass( recipes );
    }

    public <T> T parse( final InputStream in, final Class<T> type )
        throws XmlRpcException
    {
        final Recipe<?> recipe = recipesByClass.get( type );
        final XBRBinder binder = new XBRBinder( recipe, recipes );

        createParser( in ).parse( binder );

        return type.cast( binder.create() );
    }

    public <T> T parse( final Reader in, final Class<T> type )
        throws XmlRpcException
    {
        final Recipe<?> recipe = recipesByClass.get( type );
        final XBRBinder binder = new XBRBinder( recipe, recipes );

        createParser( in ).parse( binder );

        return type.cast( binder.create() );
    }

    public <T> T parse( final String in, final Class<T> type )
        throws XmlRpcException
    {
        final Recipe<?> recipe = recipesByClass.get( type );
        final XBRBinder binder = new XBRBinder( recipe, recipes );

        createParser( in ).parse( binder );

        return type.cast( binder.create() );
    }

    public <T> T parse( final XmlRpcGenerator in, final Class<T> type )
        throws XmlRpcException
    {
        final Recipe<?> recipe = recipesByClass.get( type );
        final XBRBinder binder = new XBRBinder( recipe, recipes );

        in.generate( binder );

        return type.cast( binder.create() );
    }

    protected XmlRpcParser createParser( final InputStream in )
        throws XmlRpcException
    {
        return new StaxParser( in );
    }

    protected XmlRpcParser createParser( final Reader in )
        throws XmlRpcException
    {
        return new StaxParser( in );
    }

    protected XmlRpcParser createParser( final String in )
        throws XmlRpcException
    {
        return new StaxParser( in );
    }

}
