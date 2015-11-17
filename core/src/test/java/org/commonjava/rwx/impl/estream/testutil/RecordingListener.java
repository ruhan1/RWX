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
package org.commonjava.rwx.impl.estream.testutil;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.List;

public class RecordingListener
    implements XmlRpcListener
{

    private final List<RecordedEvent> events = new ArrayList<RecordedEvent>();

    public List<RecordedEvent> getRecordedEvents()
    {
        return events;
    }

    @Override
    public RecordingListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.ARRAY_ELEMENT, index, value, type ) );
        return this;
    }

    @Override
    public RecordingListener endArray()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_ARRAY, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener endRequest()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_REQUEST, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener endResponse()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_RESPONSE, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener endStruct()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_STRUCT, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener fault( final int code, final String message )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.FAULT, code, message, ValueType.STRING ) );
        return this;
    }

    @Override
    public RecordingListener startParameter( final int index )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_PARAMETER, index, null, null ) );
        return this;
    }

    @Override
    public RecordingListener endParameter()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_PARAMETER, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener parameter( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.PARAMETER, index, value, type ) );
        return this;
    }

    @Override
    public RecordingListener requestMethod( final String methodName )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.REQUEST_METHOD, null, methodName, ValueType.STRING ) );
        return this;
    }

    @Override
    public RecordingListener startArray()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_ARRAY, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener startRequest()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_REQUEST, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener startResponse()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_RESPONSE, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener startStruct()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_STRUCT, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.STRUCT_MEMBER, key, value, type ) );
        return this;
    }

    @Override
    public RecordingListener endArrayElement()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_ARRAY_ELEMENT, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener endStructMember()
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.END_STRUCT_MEMBER, null, null, null ) );
        return this;
    }

    @Override
    public RecordingListener startArrayElement( final int index )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_ARRAY_ELEMENT, index, null, null ) );
        return this;
    }

    @Override
    public RecordingListener startStructMember( final String key )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.START_STRUCT_MEMBER, key, null, null ) );
        return this;
    }

    @Override
    public RecordingListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        events.add( new RecordedEvent( EventType.VALUE, null, value, type ) );
        return this;
    }

}
