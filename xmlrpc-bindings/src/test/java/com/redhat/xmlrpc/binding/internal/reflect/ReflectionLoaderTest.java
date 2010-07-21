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

package com.redhat.xmlrpc.binding.internal.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.redhat.xmlrpc.binding.error.BindException;
import com.redhat.xmlrpc.binding.recipe.ArrayRecipe;
import com.redhat.xmlrpc.binding.recipe.FieldBinding;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.binding.recipe.StructRecipe;
import com.redhat.xmlrpc.binding.testutil.AnnotatedAddress;
import com.redhat.xmlrpc.binding.testutil.ArrayAddress;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponse;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponse2;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponse3;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponse4;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponse5;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponseWithFinalFields;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponseWithRawAddress;
import com.redhat.xmlrpc.binding.testutil.ComposedPersonResponseWithTwoAddresses;
import com.redhat.xmlrpc.binding.testutil.ConstructedPersonRequest;
import com.redhat.xmlrpc.binding.testutil.ConstructedPersonResponse;
import com.redhat.xmlrpc.binding.testutil.InvalidAddressResponse;
import com.redhat.xmlrpc.binding.testutil.InvalidAddressResponse2;
import com.redhat.xmlrpc.binding.testutil.InvalidFinalRequest;
import com.redhat.xmlrpc.binding.testutil.OverlappingDataIndexRequest;
import com.redhat.xmlrpc.binding.testutil.OverlappingDataKeyRequest;
import com.redhat.xmlrpc.binding.testutil.OverlappingDataKeyRequest2;
import com.redhat.xmlrpc.binding.testutil.SimpleAddress;
import com.redhat.xmlrpc.binding.testutil.SimpleFinalFieldAddress;
import com.redhat.xmlrpc.binding.testutil.SimplePersonRequest;
import com.redhat.xmlrpc.binding.testutil.SimplePersonResponse;
import com.redhat.xmlrpc.binding.testutil.StructAddressWithIgnored;
import com.redhat.xmlrpc.binding.testutil.StructAddressWithTransient;
import com.redhat.xmlrpc.binding.testutil.TransientDataIndexRequest;

import java.util.Map;

public class ReflectionLoaderTest
{

    @Test
    public void simplePersonRequest()
        throws BindException
    {
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( SimplePersonRequest.class );

        assertEquals( 1, recipes.size() );

        final Recipe<?> recipe = recipes.get( SimplePersonRequest.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 1, ar.getFieldBindings().size() );

        assertEquals( "userId", ar.getFieldBinding( 0 ).getFieldName() );
    }

    @Test
    public void cacheRecipes()
        throws BindException
    {
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( SimplePersonRequest.class );
        final Map<Class<?>, Recipe<?>> recipes2 = loader.loadRecipes( SimplePersonRequest.class );

        for ( final Map.Entry<Class<?>, Recipe<?>> entry : recipes.entrySet() )
        {
            assertSame( "Recipe not cached for: " + entry.getKey().getName(), entry.getValue(),
                        recipes2.get( entry.getKey() ) );
        }
    }

    @Test
    public void simplePersonResponse()
        throws BindException
    {
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( SimplePersonResponse.class );

        assertEquals( 1, recipes.size() );

        final Recipe<?> recipe = recipes.get( SimplePersonResponse.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ConstructedPersonRequest.class );

        assertEquals( 1, recipes.size() );

        final Recipe<?> recipe = recipes.get( ConstructedPersonRequest.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ConstructedPersonResponse.class );

        assertEquals( 1, recipes.size() );

        final Recipe<?> recipe = recipes.get( ConstructedPersonResponse.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
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
        new ReflectionLoader().loadRecipes( SimpleAddress.class );
    }

    @Test( expected = BindException.class )
    public void overlappingDataIndex()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( OverlappingDataIndexRequest.class );
    }

    @Test( expected = BindException.class )
    public void overlappingDataKey()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( OverlappingDataKeyRequest.class );
    }

    @Test( expected = BindException.class )
    public void overlappingDataKeyWithFieldName()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( OverlappingDataKeyRequest2.class );
    }

    @Test( expected = BindException.class )
    public void transientFieldWithDataKeyAnnotation()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( InvalidAddressResponse.class );
    }

    @Test( expected = BindException.class )
    public void transientFieldWithDataIndexAnnotation()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( TransientDataIndexRequest.class );
    }

    @Test( expected = BindException.class )
    public void finalFieldWithInvalidDataKeyAnnotation()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( InvalidAddressResponse2.class );
    }

    @Test( expected = BindException.class )
    public void finalFieldWithInvalidDataIndexAnnotation()
        throws BindException
    {
        new ReflectionLoader().loadRecipes( InvalidFinalRequest.class );
    }

    @Test
    public void personResponseWithStructAddress()
        throws BindException
    {
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponse.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponse.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( SimpleAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( SimpleAddress.class );
        assertTrue( recipe instanceof StructRecipe );

        final StructRecipe sr = (StructRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponseWithFinalFields.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponseWithFinalFields.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( SimpleFinalFieldAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( SimpleFinalFieldAddress.class );
        assertTrue( recipe instanceof StructRecipe );

        final StructRecipe sr = (StructRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponse2.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponse2.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( AnnotatedAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( AnnotatedAddress.class );
        assertTrue( recipe instanceof StructRecipe );

        final StructRecipe sr = (StructRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponse3.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponse3.class );
        assertTrue( recipe instanceof ArrayRecipe );

        ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( ArrayAddress.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( ArrayAddress.class );
        assertTrue( recipe instanceof ArrayRecipe );

        ar = (ArrayRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponse4.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponse4.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( StructAddressWithIgnored.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( StructAddressWithIgnored.class );
        assertTrue( recipe instanceof StructRecipe );

        final StructRecipe sr = (StructRecipe) recipe;
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
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponse5.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponse5.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( StructAddressWithTransient.class, ar.getFieldBinding( i ).getFieldType() );

        recipe = recipes.get( StructAddressWithTransient.class );
        assertTrue( recipe instanceof StructRecipe );

        final StructRecipe sr = (StructRecipe) recipe;
        assertNotNull( sr.getFieldBinding( "AddressLine1" ) );
        assertNotNull( sr.getFieldBinding( "AddressLine2" ) );
        assertNotNull( sr.getFieldBinding( "City" ) );
        assertNotNull( sr.getFieldBinding( "State" ) );
        assertNotNull( sr.getFieldBinding( "ZipCode" ) );
        assertNull( sr.getFieldBinding( "unused" ) );
    }

    @Test
    public void personResponseWithRawAddressRefToStructPartClass()
        throws BindException
    {
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponseWithRawAddress.class );

        assertEquals( 1, recipes.size() );

        final Recipe<?> recipe = recipes.get( ComposedPersonResponseWithRawAddress.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
        assertEquals( 5, ar.getFieldBindings().size() );

        int i = 0;
        assertEquals( "userId", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "firstName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "lastName", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "email", ar.getFieldBinding( i++ ).getFieldName() );
        assertEquals( "address", ar.getFieldBinding( i ).getFieldName() );
        assertSame( SimpleAddress.class, ar.getFieldBinding( i ).getFieldType() );
        assertTrue( ar.getFieldBinding( i ).isRaw() );
    }

    @Test
    public void personResponseWithTwoAddresses()
        throws BindException
    {
        final ReflectionLoader loader = new ReflectionLoader();
        final Map<Class<?>, Recipe<?>> recipes = loader.loadRecipes( ComposedPersonResponseWithTwoAddresses.class );

        assertEquals( 2, recipes.size() );

        Recipe<?> recipe = recipes.get( ComposedPersonResponseWithTwoAddresses.class );
        assertTrue( recipe instanceof ArrayRecipe );

        final ArrayRecipe ar = (ArrayRecipe) recipe;
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
        assertTrue( recipe instanceof StructRecipe );

        final StructRecipe sr = (StructRecipe) recipe;
        assertNotNull( sr.getFieldBinding( "line1" ) );
        assertNotNull( sr.getFieldBinding( "line2" ) );
        assertNotNull( sr.getFieldBinding( "city" ) );
        assertNotNull( sr.getFieldBinding( "state" ) );
        assertNotNull( sr.getFieldBinding( "zip" ) );
    }

}
