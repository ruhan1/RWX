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
