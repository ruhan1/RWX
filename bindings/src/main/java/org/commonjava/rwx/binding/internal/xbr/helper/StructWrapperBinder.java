/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.binding.internal.xbr.helper;

import static org.commonjava.rwx.binding.anno.AnnotationUtils.hasAnnotation;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructWrapperBinder
    extends AbstractBinder
    implements Binder
{

    private Object value;

    public StructWrapperBinder( final Binder parent, final Class<?> valueType, final XBRBindingContext context )
    {
        super( parent, valueType, context );
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "Setting up struct wrapper binder for: {}", valueType.getName() );
    }

    @Override
    protected Binder endStructInternal()
        throws XmlRpcException
    {
        @SuppressWarnings( "unchecked" )
        final ValueType vt = hasAnnotation( getType(), ArrayPart.class ) ? ValueType.ARRAY : ValueType.STRUCT;
        setValue( value, vt );
        return this;
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( value != null )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.trace( "Setting value: {}" );
            this.value = value;
        }

        return this;
    }

}
