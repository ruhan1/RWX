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
