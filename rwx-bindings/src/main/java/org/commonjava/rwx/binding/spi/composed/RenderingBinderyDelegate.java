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


import java.io.InputStream;
import java.io.Reader;

public abstract class RenderingBinderyDelegate
    implements Bindery
{

    @Override
    public final <T> T parse( final InputStream in, final Class<T> type )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a parsing delegate." );
    }

    @Override
    public final <T> T parse( final Reader in, final Class<T> type )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a parsing delegate." );
    }

    @Override
    public final <T> T parse( final String in, final Class<T> type )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a parsing delegate." );
    }

    @Override
    public final <T> T parse( final XmlRpcGenerator in, final Class<T> type )
        throws XmlRpcException
    {
        throw new UnsupportedOperationException(
                                                 "Not supported. This delegate is meant to work in conjunction with a parsing delegate." );
    }

}
