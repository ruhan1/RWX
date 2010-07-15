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

package com.redhat.xmlrpc.binding.internal.reflect;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.redhat.xmlrpc.binding.internal.RenderingBinderyDelegate;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.jdom.JDomRenderer;
import com.redhat.xmlrpc.spi.XmlRpcListener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

public class ReflectionUnbindery
    extends RenderingBinderyDelegate
{

    private final Collection<Recipe<?>> recipes;

    private final XMLOutputter outputter;

    public ReflectionUnbindery( final Collection<Recipe<?>> recipes )
    {
        this.recipes = recipes;
        outputter = new XMLOutputter( Format.getCompactFormat() );
    }

    public ReflectionUnbindery( final Collection<Recipe<?>> recipes, final XMLOutputter outputter )
    {
        this.recipes = recipes;
        this.outputter = outputter == null ? new XMLOutputter( Format.getCompactFormat() ) : outputter;
    }

    @Override
    public void render( final OutputStream out, final Object value )
        throws XmlRpcException
    {
        final JDomRenderer renderer = new JDomRenderer();
        new ReflectionUnbinder( value, recipes ).generate( renderer );
        try
        {
            outputter.output( renderer.getDocument(), out );
        }
        catch ( final IOException e )
        {
            throw new XmlRpcException( "Failed to render to stream: " + e.getMessage(), e );
        }
    }

    @Override
    public void render( final Writer out, final Object value )
        throws XmlRpcException
    {
        final JDomRenderer renderer = new JDomRenderer();
        new ReflectionUnbinder( value, recipes ).generate( renderer );
        try
        {
            outputter.output( renderer.getDocument(), out );
        }
        catch ( final IOException e )
        {
            throw new XmlRpcException( "Failed to render to stream: " + e.getMessage(), e );
        }
    }

    @Override
    public void render( final XmlRpcListener out, final Object value )
        throws XmlRpcException
    {
        new ReflectionUnbinder( value, recipes ).generate( out );
    }

    @Override
    public String renderString( final Object value )
        throws XmlRpcException
    {
        final JDomRenderer renderer = new JDomRenderer();
        new ReflectionUnbinder( value, recipes ).generate( renderer );

        return outputter.outputString( renderer.getDocument() );
    }

}
