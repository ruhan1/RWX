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

package org.commonjava.rwx.binding.spi.composed;

import org.commonjava.rwx.binding.spi.Bindery;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class CompositionBindery
    implements Bindery
{

    private final RenderingBinderyDelegate renderer;

    private final ParsingBinderyDelegate parser;

    public CompositionBindery( final RenderingBinderyDelegate renderer, final ParsingBinderyDelegate parser )
    {
        this.renderer = renderer;
        this.parser = parser;
    }

    public <T> T parse( final InputStream in, final Class<T> type )
        throws XmlRpcException
    {
        return parser.parse( in, type );
    }

    public <T> T parse( final Reader in, final Class<T> type )
        throws XmlRpcException
    {
        return parser.parse( in, type );
    }

    public <T> T parse( final String in, final Class<T> type )
        throws XmlRpcException
    {
        return parser.parse( in, type );
    }

    public <T> T parse( final XmlRpcGenerator in, final Class<T> type )
        throws XmlRpcException
    {
        return parser.parse( in, type );
    }

    public void render( final OutputStream out, final Object value )
        throws XmlRpcException
    {
        renderer.render( out, value );
    }

    public void render( final Writer out, final Object value )
        throws XmlRpcException
    {
        renderer.render( out, value );
    }

    public void render( final XmlRpcListener out, final Object value )
        throws XmlRpcException
    {
        renderer.render( out, value );
    }

    public String renderString( final Object value )
        throws XmlRpcException
    {
        return renderer.renderString( value );
    }

}
