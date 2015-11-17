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

package org.commonjava.rwx.binding.internal.reflect;

import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.composed.RenderingBinderyDelegate;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.jdom.JDomRenderer;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

public class ReflectionUnbindery
    extends RenderingBinderyDelegate
{

    private final Map<Class<?>, Mapping<?>> recipes;

    private final XMLOutputter outputter;

    public ReflectionUnbindery( final Map<Class<?>, Mapping<?>> recipes )
    {
        this.recipes = recipes;
        outputter = new XMLOutputter( Format.getCompactFormat() );
    }

    public ReflectionUnbindery( final Map<Class<?>, Mapping<?>> recipes, final XMLOutputter outputter )
    {
        this.recipes = recipes;
        this.outputter = outputter == null ? new XMLOutputter( Format.getCompactFormat() ) : outputter;
    }

    @Override
    public void render( final OutputStream out, final Object value )
        throws XmlRpcException
    {
        final Document doc = renderToDocument( value );

        try
        {
            outputter.output( doc, out );
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
        final Document doc = renderToDocument( value );

        try
        {
            outputter.output( doc, out );
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
        if ( value instanceof XmlRpcGenerator )
        {
            ( (XmlRpcGenerator) value ).generate( out );
        }
        else
        {
            new ReflectionUnbinder( value, recipes ).generate( out );
        }
    }

    @Override
    public String renderString( final Object value )
        throws XmlRpcException
    {
        final Document doc = renderToDocument( value );

        return outputter.outputString( doc );
    }

    private Document renderToDocument( final Object value )
        throws XmlRpcException
    {
        final JDomRenderer renderer = new JDomRenderer();
        if ( value instanceof XmlRpcGenerator )
        {
            ( (XmlRpcGenerator) value ).generate( renderer );
        }
        else
        {
            new ReflectionUnbinder( value, recipes ).generate( renderer );
        }

        return renderer.getDocument();
    }

}
