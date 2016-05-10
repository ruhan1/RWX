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
package org.commonjava.rwx.binding.spi.value;

import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcListener;

public abstract class AbstractSimpleValueBinder
    extends AbstractXmlRpcListener
    implements ValueBinder
{

    private final Class<?> type;

    private final BindingContext context;

    private final Binder parent;

    protected AbstractSimpleValueBinder( final Binder parent, final Class<?> type, final BindingContext context )
    {
        this.parent = parent;
        this.context = context;
        this.type = type;
    }

    public final Binder getParent()
    {
        return parent;
    }

    public final BindingContext getBindingContext()
    {
        return context;
    }

    public final Class<?> getType()
    {
        return type;
    }

    @Override
    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endArrayElement()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endStruct()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endStructMember()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startArrayElement( final int index )
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startStruct()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startStructMember( final String key )
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener endParameter()
        throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public XmlRpcListener startParameter( final int index )
        throws XmlRpcException
    {
        throw forbidden();
    }

}
