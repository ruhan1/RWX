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
import org.commonjava.rwx.binding.spi.value.ValueBinder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CustomStructBinder
    extends AbstractXmlRpcListener
    implements ValueBinder
{

    private final Class<?> type;

    private final BindingContext context;

    private final Binder parent;

    private boolean structDone;

    protected CustomStructBinder( final Binder parent, final Class<?> type, final BindingContext context )
    {
        this.parent = parent;
        this.context = context;
        this.type = type;
    }

    protected abstract void map( String key, Object v, ValueType t )
            throws XmlRpcException;

    protected abstract Object getBindingResult()
            throws XmlRpcException;

    public final BindingContext getBindingContext()
    {
        return context;
    }

    public final Class<?> getType()
    {
        return type;
    }

    public final Binder getParent()
    {
        return parent;
    }

    @Override
    public final XmlRpcListener structMember( String k, Object v, ValueType t )
            throws XmlRpcException
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "Got member: '{}' with {} value: {}", k, t, v );
        logger.trace( "Handing off to map() call." );
        map( k, v, t );
        return this;
    }

    @Override
    public XmlRpcListener endStruct()
            throws XmlRpcException
    {
        structDone = true;
        return super.endStruct();
    }

    @Override
    public final XmlRpcListener value( final Object v, final ValueType t )
        throws XmlRpcException
    {
        if ( !structDone )
        {
            return this;
        }

        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "Got {} value: {}", t, v );
        parent.value( getBindingResult(), ValueType.STRUCT );
        return parent;
    }

    @Override
    public final XmlRpcListener arrayElement( int index, Object value, ValueType type )
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endArray()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endRequest()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endResponse()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener fault( int code, String message )
            throws XmlRpcException
    {
        return super.fault( code, message );
    }

    @Override
    public final XmlRpcListener startParameter( int index )
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endParameter()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener parameter( int index, Object value, ValueType type )
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener requestMethod( String methodName )
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startArray()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startRequest()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startResponse()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startStruct()
            throws XmlRpcException
    {
        return super.startStruct();
    }

    @Override
    public final XmlRpcListener endArrayElement()
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener endStructMember()
            throws XmlRpcException
    {
        return super.endStructMember();
    }

    @Override
    public final XmlRpcListener startArrayElement( int index )
            throws XmlRpcException
    {
        throw forbidden();
    }

    @Override
    public final XmlRpcListener startStructMember( String key )
            throws XmlRpcException
    {
        return super.startStructMember( key );
    }
}
