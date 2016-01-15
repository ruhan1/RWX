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
import org.commonjava.rwx.binding.internal.xbr.helper.MessageBinder;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.composed.ParsingBinderyDelegate;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.stax.StaxParser;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcParser;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public class XBeanRenderingBindery
    extends ParsingBinderyDelegate
{

    private final XBRBindingContext context;

    public XBeanRenderingBindery( final Map<Class<?>, Mapping<?>> mappings )
        throws BindException
    {
        context = new XBRBindingContext( mappings );
    }

    public <T> T parse( final InputStream in, final Class<T> type )
        throws XmlRpcException
    {
        final MessageBinder binder = context.newMessageBinder( type );

        createParser( in ).parse( binder );

        return type.cast( binder.create() );
    }

    public <T> T parse( final Reader in, final Class<T> type )
        throws XmlRpcException
    {
        final MessageBinder binder = context.newMessageBinder( type );

        createParser( in ).parse( binder );

        return type.cast( binder.create() );
    }

    public <T> T parse( final String in, final Class<T> type )
        throws XmlRpcException
    {
        final MessageBinder binder = context.newMessageBinder( type );

        createParser( in ).parse( binder );

        return type.cast( binder.create() );
    }

    public <T> T parse( final XmlRpcGenerator in, final Class<T> type )
        throws XmlRpcException
    {
        final MessageBinder binder = context.newMessageBinder( type );

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
