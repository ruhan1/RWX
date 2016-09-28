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

import org.apache.commons.lang.StringUtils;
import org.commonjava.rwx.binding.conf.BindingConfiguration;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.composed.RenderingBinderyDelegate;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.impl.estream.EventStreamParserImpl;
import org.commonjava.rwx.impl.jdom.JDomRenderer;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

public class ReflectionUnbindery
    extends RenderingBinderyDelegate
{

    private final Map<Class<?>, Mapping<?>> recipes;

    private BindingConfiguration configuration;

    private final XMLOutputter outputter;

    public ReflectionUnbindery( final Map<Class<?>, Mapping<?>> recipes, BindingConfiguration configuration )
    {
        this.recipes = recipes;
        this.configuration = configuration;
        outputter = new XMLOutputter( Format.getCompactFormat() );
    }

    public ReflectionUnbindery( final Map<Class<?>, Mapping<?>> recipes, final XMLOutputter outputter, BindingConfiguration configuration )
    {
        this.recipes = recipes;
        this.outputter = outputter == null ? new XMLOutputter( Format.getCompactFormat() ) : outputter;
        this.configuration = configuration;
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
            new ReflectionUnbinder( value, recipes, configuration ).generate( out );
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
        Logger logger = LoggerFactory.getLogger( getClass() );

        JDomRenderer renderer = new JDomRenderer();
        TrackingXmlRpcListener tracker = null;
        EventStreamParserImpl estream = null;

        XmlRpcListener listener = renderer;
//        if ( logger.isTraceEnabled() )
//        {
            tracker = new TrackingXmlRpcListener( renderer );
            listener = tracker;
//            estream = new EventStreamParserImpl( tracker );
//            listener = estream;
//        }

        if ( value instanceof XmlRpcGenerator )
        {
            ( (XmlRpcGenerator) value ).generate( listener );
        }
        else
        {
            new ReflectionUnbinder( value, recipes, configuration ).generate( listener );
        }

//        if ( logger.isTraceEnabled() )
//        {
//            logger.trace( "Message call trace:\n\n  {}\n\nEvent tree:\n\n  {}\n\nXML:\n\n{}\n\n",
//                          StringUtils.join( tracker.getCalls(), "\n  " ), estream.renderEventTree(), renderer.documentToString() );
//        }

        return renderer.getDocument();
    }

}
