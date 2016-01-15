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
package org.commonjava.rwx.binding.internal.reflect;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.InheritedPersonRequest;
import org.commonjava.rwx.binding.testutil.RecordedEvent;
import org.commonjava.rwx.binding.testutil.RecordingListener;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleAddressMapResponse;
import org.commonjava.rwx.binding.testutil.SimpleConverterRequest;
import org.commonjava.rwx.binding.testutil.SimpleListRequest;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.binding.testutil.TestObject;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.commonjava.rwx.binding.testutil.EventAssertions.assertRecordedEvents;

public class ReflectionUnbinderTest
{

    @Test( expected = BindException.class )
    public void invalidEntryPoint()
        throws XmlRpcException
    {
        new ReflectionUnbinder( new SimpleAddress(), new HashMap<Class<?>, Mapping<?>>() ).generate( new RecordingListener() );
    }

    @Test
    public void fault()
        throws XmlRpcException
    {
        final List<Event<?>> check = new ArrayList<Event<?>>(
                Arrays.asList( new ResponseEvent( true ), new ResponseEvent( 101, "Test fault" ),
                               new ResponseEvent( false ) ) );

        final ReflectionUnbinder unbinder = new ReflectionUnbinder( new XmlRpcFaultException( 101, "Test fault" ),
                                                                    new HashMap<Class<?>, Mapping<?>>() );
        final RecordingListener listener = new RecordingListener();
        unbinder.generate( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleRequest()
        throws XmlRpcException
    {
        assertUnbind( new SimplePersonRequest() );
    }

    @Test
    public void simpleConverterRequest()
        throws XmlRpcException, ParseException
    {
        assertUnbind( new SimpleConverterRequest() );
    }

    @Test
    public void simpleListRequest()
        throws XmlRpcException
    {
        assertUnbind( new SimpleListRequest() );
    }

    @Test
    public void simpleAddressMapResponse()
        throws XmlRpcException
    {
        assertUnbind( new SimpleAddressMapResponse() );
    }

    @Test
    public void requestWithInheritedFields()
        throws XmlRpcException
    {
        assertUnbind( new InheritedPersonRequest() );
    }

    @Test
    public void responseWithStructParam()
        throws XmlRpcException
    {
        assertUnbind( new ComposedPersonResponse() );
    }

    @Test
    public void responseWithArrayParam()
        throws XmlRpcException
    {
        assertUnbind( new ComposedPersonResponse3() );
    }

    private void assertUnbind( final TestObject request )
        throws XmlRpcException
    {
        final ReflectionUnbinder unbinder = new ReflectionUnbinder( request, request.recipes() );

        final RecordingListener listener = new RecordingListener();
        unbinder.generate( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        assertRecordedEvents( request.events(), events );
    }

}
