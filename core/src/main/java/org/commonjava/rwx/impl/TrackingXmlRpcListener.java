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
package org.commonjava.rwx.impl;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TrackingXmlRpcListener
        implements XmlRpcListener
{

    private XmlRpcListener current;

    private XmlRpcListener last;

    private final XmlRpcListener root;

    public TrackingXmlRpcListener( final XmlRpcListener root )
    {
        this.root = root;
        current = root;
        last = null;
    }

    public XmlRpcListener getRoot()
    {
        return root;
    }

    public XmlRpcListener getCurrent()
    {
        return current;
    }

    public XmlRpcListener getLast()
    {
        return last;
    }

    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
            throws XmlRpcException
    {
        return record( () -> current.arrayElement( index, value, type ) );
    }

    public XmlRpcListener endArray()
            throws XmlRpcException
    {
        return record( () -> current.endArray() );
    }

    public XmlRpcListener endArrayElement()
            throws XmlRpcException
    {
        return record( () -> current.endArrayElement() );
    }

    public XmlRpcListener endParameter()
            throws XmlRpcException
    {
        return record( () -> current.endParameter() );
    }

    public XmlRpcListener endRequest()
            throws XmlRpcException
    {
        return record( () -> current.endRequest() );
    }

    public XmlRpcListener endResponse()
            throws XmlRpcException
    {
        return record( () -> current.endResponse() );
    }

    public XmlRpcListener endStruct()
            throws XmlRpcException
    {
        return record( () -> current.endStruct() );
    }

    public XmlRpcListener endStructMember()
            throws XmlRpcException
    {
        return record( () -> current.endStructMember() );
    }

    public XmlRpcListener fault( final int code, final String message )
            throws XmlRpcException
    {
        return record( () -> current.fault( code, message ) );
    }

    public XmlRpcListener parameter( final int index, final Object value, final ValueType type )
            throws XmlRpcException
    {
        return record( () -> current.parameter( index, value, type ) );
    }

    public XmlRpcListener requestMethod( final String methodName )
            throws XmlRpcException
    {
        return record( () -> current.requestMethod( methodName ) );
    }

    public XmlRpcListener startArray()
            throws XmlRpcException
    {
        return record( () -> current.startArray() );
    }

    public XmlRpcListener startArrayElement( final int index )
            throws XmlRpcException
    {
        return record( () -> current.startArrayElement( index ) );
    }

    public XmlRpcListener startParameter( final int index )
            throws XmlRpcException
    {
        return record( () -> current.startParameter( index ) );
    }

    public XmlRpcListener startRequest()
            throws XmlRpcException
    {
        return record( () -> current.startRequest() );
    }

    public XmlRpcListener startResponse()
            throws XmlRpcException
    {
        return record( () -> current.startResponse() );
    }

    public XmlRpcListener startStruct()
            throws XmlRpcException
    {
        return record( () -> current.startStruct() );
    }

    public XmlRpcListener startStructMember( final String key )
            throws XmlRpcException
    {
        return record( () -> current.startStructMember( key ) );
    }

    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
            throws XmlRpcException
    {
        return record( () -> current.structMember( key, value, type ) );
    }

    public XmlRpcListener value( final Object value, final ValueType type )
            throws XmlRpcException
    {
        return record( () -> current.value( value, type ) );
    }

    private List<XmlRpcCall> calls = new ArrayList<>();

    public static final class XmlRpcCall
    {
        private StackTraceElement frame;
        private XmlRpcListener last;
        private XmlRpcListener current;
        private XmlRpcListener next;

        public XmlRpcCall( StackTraceElement frame, XmlRpcListener last, XmlRpcListener current, XmlRpcListener next )
        {
            this.frame = frame;
            this.last = last;
            this.current = current;
            this.next = next;
        }

        public StackTraceElement getFrame()
        {
            return frame;
        }

        public XmlRpcListener getLast()
        {
            return last;
        }

        public XmlRpcListener getCurrent()
        {
            return current;
        }

        public XmlRpcListener getNext()
        {
            return next;
        }

        @Override
        public String toString()
        {
            return "XmlRpcCall{\n\t" +
                    "frame=" + frame +
                    "\n\tlast=" + last +
                    "\n\tcurrent=" + current +
                    "\n\tnext=" + next +
                    "\n}";
        }
    }

    private XmlRpcListener record( RecordingOp<XmlRpcListener> supplier )
            throws XmlRpcException
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        StackTraceElement lastFrame = Thread.currentThread().getStackTrace()[2];
        logger.trace( "CALLING: {}.{}\n  {} -> {}", lastFrame.getClassName(), lastFrame.getMethodName(), last, current );

        XmlRpcListener next = supplier.execute();
        calls.add( new XmlRpcCall( lastFrame, last, current, next ) );

        logger.trace( "CALLED: {}.{}\n  {} -> {} -> {}", lastFrame.getClassName(), lastFrame.getMethodName(), last, current,
                      next );

        last = current;
        current = next;

        return next;
    }

    public List<XmlRpcCall> getCalls()
    {
        return calls;
    }

    public interface RecordingOp<T>
    {
        XmlRpcListener execute()
                throws XmlRpcException;
    }
}
