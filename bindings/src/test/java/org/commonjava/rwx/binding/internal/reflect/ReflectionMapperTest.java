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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.StructMapping;
import org.commonjava.rwx.binding.testutil.AnnotatedAddress;
import org.commonjava.rwx.binding.testutil.ArrayAddress;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse2;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse4;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse5;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponseWithFinalFields;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponseWithTwoAddresses;
import org.commonjava.rwx.binding.testutil.ConstructedPersonRequest;
import org.commonjava.rwx.binding.testutil.ConstructedPersonResponse;
import org.commonjava.rwx.binding.testutil.InvalidAddressResponse;
import org.commonjava.rwx.binding.testutil.InvalidAddressResponse2;
import org.commonjava.rwx.binding.testutil.InvalidFinalRequest;
import org.commonjava.rwx.binding.testutil.OverlappingDataIndexRequest;
import org.commonjava.rwx.binding.testutil.OverlappingDataKeyRequest;
import org.commonjava.rwx.binding.testutil.OverlappingDataKeyRequest2;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleAddressMapResponse;
import org.commonjava.rwx.binding.testutil.SimpleConverterRequest;
import org.commonjava.rwx.binding.testutil.SimpleFinalFieldAddress;
import org.commonjava.rwx.binding.testutil.SimpleListRequest;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.binding.testutil.SimplePersonResponse;
import org.commonjava.rwx.binding.testutil.StructAddressWithIgnored;
import org.commonjava.rwx.binding.testutil.StructAddressWithTransient;
import org.commonjava.rwx.binding.testutil.TestDateConverter;
import org.commonjava.rwx.binding.testutil.TransientDataIndexRequest;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ReflectionMapperTest
{

    @Test
    public void simplePersonRequest()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( SimplePersonRequest.class );

        assertEquals( 2, recipes.size() );

        final Mapping<?> recipe = recipes.get( SimplePersonRequest.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 1, ar.getFieldBindings().size() );

        assertEquals( "userId", ar.getFieldBinding( 0 ).getFieldName() );
    }

    @Test
    public void simpleRequestWithConverter()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( SimpleConverterRequest.class );

        assertEquals( 2, recipes.size() );

        final Mapping<?> recipe = recipes.get( SimpleConverterRequest.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 1, ar.getFieldBindings().size() );

        assertEquals( "date", ar.getFieldBinding( 0 ).getFieldName() );
        assertEquals( TestDateConverter.class, ar.getFieldBinding( 0 ).getValueBinderType() );
    }

    @Test
    public void simpleRequestWithList()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( SimpleListRequest.class );

        assertEquals( 2, recipes.size() );

        final Mapping<?> recipe = recipes.get( SimpleListRequest.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 1, ar.getFieldBindings().size() );

        assertEquals( "userIds", ar.getFieldBinding( 0 ).getFieldName() );
        assertEquals( List.class, ar.getFieldBinding( 0 ).getFieldType() );
    }

    @Test
    public void simpleResponseWithMapOfAddresses()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( SimpleAddressMapResponse.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( SimpleAddressMapResponse.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 1, ar.getFieldBindings().size() );

        assertEquals( "addresses", ar.getFieldBinding( 0 ).getFieldName() );
        assertEquals( Map.class, ar.getFieldBinding( 0 ).getFieldType() );

        recipe = recipes.get( SimpleAddress.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "line1" ) );
        assertNotNull( sr.getFieldBinding( "line2" ) );
        assertNotNull( sr.getFieldBinding( "city" ) );
        assertNotNull( sr.getFieldBinding( "state" ) );
        assertNotNull( sr.getFieldBinding( "zip" ) );
    }

    @Test
    public void cacheRecipes()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( SimplePersonRequest.class );
        final Map<Class<?>, Mapping<?>> recipes2 = loader.loadRecipes( SimplePersonRequest.class );

        for ( final Map.Entry<Class<?>, Mapping<?>> entry : recipes.entrySet() )
        {
            assertSame( "Recipe not cached for: " + entry.getKey().getName(), entry.getValue(),
                        recipes2.get( entry.getKey() ) );
        }
    }

    @Test
    public void simplePersonResponse()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( SimplePersonResponse.class );

        assertEquals( 2, recipes.size() );

        final Mapping<?> recipe = recipes.get( SimplePersonResponse.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 4, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
    }

    @Test
    public void constructedPersonRequest()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ConstructedPersonRequest.class );

        assertEquals( 2, recipes.size() );

        final Mapping<?> recipe = recipes.get( ConstructedPersonRequest.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 1, ar.getFieldBindings().size() );

        assertEquals( "userId", ar.getFieldBinding( 0 ).getFieldName() );

        final Integer[] keys = ar.getConstructorKeys();
        assertEquals( 1, keys.length );
        assertEquals( 0, keys[0].intValue() );
    }

    @Test
    public void constructedPersonResponse()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ConstructedPersonResponse.class );

        assertEquals( 2, recipes.size() );

        final Mapping<?> recipe = recipes.get( ConstructedPersonResponse.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 4, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );

        final Integer[] keys = ar.getConstructorKeys();
        assertEquals( 2, keys.length );
        assertEquals( 0, keys[0].intValue() );
        assertEquals( 3, keys[1].intValue() );
    }

    @Test( expected = BindException.class )
    public void invalidEntryPointClass()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( SimpleAddress.class );
    }

    @Test( expected = BindException.class )
    public void overlappingDataIndex()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( OverlappingDataIndexRequest.class );
    }

    @Test( expected = BindException.class )
    public void overlappingDataKey()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( OverlappingDataKeyRequest.class );
    }

    @Test( expected = BindException.class )
    public void overlappingDataKeyWithFieldName()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( OverlappingDataKeyRequest2.class );
    }

    @Test( expected = BindException.class )
    public void transientFieldWithDataKeyAnnotation()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( InvalidAddressResponse.class );
    }

    @Test( expected = BindException.class )
    public void transientFieldWithDataIndexAnnotation()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( TransientDataIndexRequest.class );
    }

    @Test( expected = BindException.class )
    public void finalFieldWithInvalidDataKeyAnnotation()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( InvalidAddressResponse2.class );
    }

    @Test( expected = BindException.class )
    public void finalFieldWithInvalidDataIndexAnnotation()
        throws BindException
    {
        new ReflectionMapper().loadRecipes( InvalidFinalRequest.class );
    }

    @Test
    public void personResponseWithStructAddress()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponse.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponse.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( SimpleAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( SimpleAddress.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "line1" ) );
        assertNotNull( sr.getFieldBinding( "line2" ) );
        assertNotNull( sr.getFieldBinding( "city" ) );
        assertNotNull( sr.getFieldBinding( "state" ) );
        assertNotNull( sr.getFieldBinding( "zip" ) );
    }

    @Test
    public void finalFieldsReferencedInConstructAnnos()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponseWithFinalFields.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponseWithFinalFields.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( SimpleFinalFieldAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( SimpleFinalFieldAddress.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "line1" ) );
        assertNotNull( sr.getFieldBinding( "line2" ) );
        assertNotNull( sr.getFieldBinding( "city" ) );
        assertNotNull( sr.getFieldBinding( "state" ) );
        assertNotNull( sr.getFieldBinding( "zip" ) );
    }

    @Test
    public void personResponseWithAnnotatedStructAddress()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponse2.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponse2.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( AnnotatedAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( AnnotatedAddress.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "AddressLine1" ) );
        assertNotNull( sr.getFieldBinding( "AddressLine2" ) );
        assertNotNull( sr.getFieldBinding( "City" ) );
        assertNotNull( sr.getFieldBinding( "State" ) );
        assertNotNull( sr.getFieldBinding( "ZipCode" ) );
    }

    @Test
    public void personResponseWithArrayAddress()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponse3.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponse3.class );
        assertTrue( recipe instanceof ArrayMapping );

        ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( ArrayAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( ArrayAddress.class );
        assertTrue( recipe instanceof ArrayMapping );

        ar = (ArrayMapping) recipe;
        i = 0;

        assertEquals( "line1", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "line2", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "city", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "state", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "zip", ar.getFieldBinding( i++ ).getFieldName() );
    }

    @Test
    public void personResponseWithStructAddressContainingIgnoredField()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponse4.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponse4.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( StructAddressWithIgnored.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( StructAddressWithIgnored.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "AddressLine1" ) );
        assertNotNull( sr.getFieldBinding( "AddressLine2" ) );
        assertNotNull( sr.getFieldBinding( "City" ) );
        assertNotNull( sr.getFieldBinding( "State" ) );
        assertNotNull( sr.getFieldBinding( "ZipCode" ) );
        assertNull( sr.getFieldBinding( "unused" ) );
    }

    @Test
    public void personResponseWithStructAddressContainingTransientField()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponse5.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponse5.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( StructAddressWithTransient.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( StructAddressWithTransient.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "AddressLine1" ) );
        assertNotNull( sr.getFieldBinding( "AddressLine2" ) );
        assertNotNull( sr.getFieldBinding( "City" ) );
        assertNotNull( sr.getFieldBinding( "State" ) );
        assertNotNull( sr.getFieldBinding( "ZipCode" ) );
        assertNull( sr.getFieldBinding( "unused" ) );
    }

    @Test
    public void personResponseWithTwoAddresses()
        throws BindException
    {
        final ReflectionMapper loader = new ReflectionMapper();
        final Map<Class<?>, Mapping<?>> recipes = loader.loadRecipes( ComposedPersonResponseWithTwoAddresses.class );

        assertEquals( 3, recipes.size() );

        Mapping<?> recipe = recipes.get( ComposedPersonResponseWithTwoAddresses.class );
        assertTrue( recipe instanceof ArrayMapping );

        final ArrayMapping ar = (ArrayMapping) recipe;
        assertEquals( 6, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );

        FieldBinding binding = ar.getFieldBinding( i++ );
        assertEquals( "homeAddress", binding.getFieldName() );
        assertSame( SimpleAddress.class, binding.getFieldType() );

        binding = ar.getFieldBinding( i++ );
        assertEquals( "businessAddress", binding.getFieldName() );
        assertSame( SimpleAddress.class, binding.getFieldType() );

        recipe = recipes.get( SimpleAddress.class );
        assertTrue( recipe instanceof StructMapping );

        final StructMapping sr = (StructMapping) recipe;
        assertNotNull( sr.getFieldBinding( "line1" ) );
        assertNotNull( sr.getFieldBinding( "line2" ) );
        assertNotNull( sr.getFieldBinding( "city" ) );
        assertNotNull( sr.getFieldBinding( "state" ) );
        assertNotNull( sr.getFieldBinding( "zip" ) );
    }

}
