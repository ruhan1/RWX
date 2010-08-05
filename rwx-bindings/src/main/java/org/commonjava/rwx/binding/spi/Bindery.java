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

package org.commonjava.rwx.binding.spi;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface Bindery
{

    <T> T parse( final InputStream in, final Class<T> type )
        throws XmlRpcException;

    <T> T parse( final Reader in, final Class<T> type )
        throws XmlRpcException;

    <T> T parse( final String in, final Class<T> type )
        throws XmlRpcException;

    <T> T parse( final XmlRpcGenerator in, final Class<T> type )
        throws XmlRpcException;

    void render( final OutputStream out, final Object value )
        throws XmlRpcException;

    void render( final Writer out, final Object value )
        throws XmlRpcException;

    void render( final XmlRpcListener out, final Object value )
        throws XmlRpcException;

    String renderString( final Object value )
        throws XmlRpcException;

}
