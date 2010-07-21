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

package org.commonjava.rwx.binding.internal.xbr;

import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.xbr.XBRBinder;
import org.commonjava.rwx.binding.recipe.Recipe;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponseWithFinalFields;
import org.commonjava.rwx.binding.testutil.InheritedPersonRequest;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleFinalFieldAddress;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.binding.testutil.TestObject;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.impl.estream.EventStreamGenerator;
import org.junit.Test;


import java.util.HashMap;
import java.util.Map;

public class XBRBinderTest
{

    @Test
    public void simpleRequest()
        throws XmlRpcException
    {
        assertBindings( new SimplePersonRequest() );
    }

    @Test( expected = BindException.class )
    public void invalidEntryPoint()
        throws XmlRpcException
    {
        new XBRBinder<SimpleAddress>( SimpleAddress.class, new HashMap<Class<?>, Recipe<?>>() );
    }

    @Test( expected = XmlRpcFaultException.class )
    public void fault()
        throws XmlRpcException
    {
        final XBRBinder<SimplePersonRequest> binder =
            new XBRBinder<SimplePersonRequest>( SimplePersonRequest.class, new SimplePersonRequest().recipes() );

        binder.startResponse().fault( 101, "Test fault" ).endResponse();
    }

    @Test
    public void requestWithInheritedFields()
        throws XmlRpcException
    {
        assertBindings( new InheritedPersonRequest() );
    }

    @Test
    public void responseWithStructParam()
        throws XmlRpcException
    {
        assertBindings( new ComposedPersonResponse() );
    }

    @Test
    public void responseWithArrayParam()
        throws XmlRpcException
    {
        assertBindings( new ComposedPersonResponse3() );
    }

    @Test
    public void responseWithConstructorAnnotations()
        throws XmlRpcException
    {
        assertBindings( new ComposedPersonResponseWithFinalFields( "foo", "foo@nowhere.com",
                                                                   new SimpleFinalFieldAddress( "123 Sesame St",
                                                                                                "Little Big Fork",
                                                                                                "NV", "01234" ) ) );
    }

    @SuppressWarnings( "unchecked" )
    private <T extends TestObject> void assertBindings( final T object )
        throws XmlRpcException
    {
        final Map<Class<?>, Recipe<?>> recipes = object.recipes();

        final XBRBinder<T> binder = new XBRBinder<T>( (Class<T>) object.getClass(), recipes );

        new EventStreamGenerator( object.events() ).generate( binder );

        final T result = binder.create();

        assertEquals( object, result );
    }

}
