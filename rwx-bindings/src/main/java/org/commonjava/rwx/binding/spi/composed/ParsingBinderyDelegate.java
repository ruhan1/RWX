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

package org.commonjava.rwx.binding.spi.composed;

import org.commonjava.rwx.binding.spi.Bindery;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;


import java.io.OutputStream;
import java.io.Writer;

public abstract class ParsingBinderyDelegate
    implements Bindery
{

    @Override
    public final void render( final OutputStream out, final Object value )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a rendering delegate." );
    }

    @Override
    public final void render( final Writer out, final Object value )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a rendering delegate." );
    }

    @Override
    public final void render( final XmlRpcListener out, final Object value )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a rendering delegate." );
    }

    @Override
    public final String renderString( final Object value )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a rendering delegate." );
    }

}
