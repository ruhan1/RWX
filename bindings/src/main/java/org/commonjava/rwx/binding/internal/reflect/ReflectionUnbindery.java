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
