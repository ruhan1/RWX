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

package com.redhat.xmlrpc.binding.testutil;

import static com.redhat.xmlrpc.binding.testutil.recipe.RecipeEventUtils.endParameter;
import static com.redhat.xmlrpc.binding.testutil.recipe.RecipeEventUtils.parameter;
import static com.redhat.xmlrpc.binding.testutil.recipe.RecipeEventUtils.stringArray;

import com.redhat.xmlrpc.binding.anno.DataIndex;
import com.redhat.xmlrpc.binding.anno.Response;
import com.redhat.xmlrpc.binding.recipe.ArrayRecipe;
import com.redhat.xmlrpc.binding.recipe.FieldBinding;
import com.redhat.xmlrpc.binding.recipe.Recipe;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.ParameterEvent;
import com.redhat.xmlrpc.impl.estream.model.ResponseEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.vocab.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Response
public class ComposedPersonResponse3
    implements TestObject
{

    @DataIndex( 0 )
    private String userId = "bart";

    @DataIndex( 1 )
    private String firstName = "Bart";

    @DataIndex( 2 )
    private String lastName = "Simpson";

    @DataIndex( 3 )
    private String email = "bsimp@nowhere.com";

    @DataIndex( 4 )
    private ArrayAddress address = new ArrayAddress();

    public ArrayAddress getAddress()
    {
        return address;
    }

    public void setAddress( final ArrayAddress address )
    {
        this.address = address;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( final String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( final String lastName )
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( final String email )
    {
        this.email = email;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( final String userId )
    {
        this.userId = userId;
    }

    public Map<Class<?>, Recipe<?>> recipes()
    {
        final Map<Class<?>, Recipe<?>> recipes = new HashMap<Class<?>, Recipe<?>>();

        final ArrayRecipe recipe = new ArrayRecipe( ComposedPersonResponse3.class, new Integer[0] );

        recipe.addFieldBinding( 0, new FieldBinding( "userId", String.class, true ) )
              .addFieldBinding( 1, new FieldBinding( "firstName", String.class, true ) )
              .addFieldBinding( 2, new FieldBinding( "lastName", String.class, true ) )
              .addFieldBinding( 3, new FieldBinding( "email", String.class, true ) )
              .addFieldBinding( 4, new FieldBinding( "address", ArrayAddress.class, false ) );

        recipes.put( ComposedPersonResponse3.class, recipe );

        // ArrayAddress
        final ArrayRecipe aRecipe = new ArrayRecipe( ArrayAddress.class, new Integer[0] );

        aRecipe.addFieldBinding( 0, new FieldBinding( "line1", String.class, true ) )
               .addFieldBinding( 1, new FieldBinding( "line2", String.class, true ) )
               .addFieldBinding( 2, new FieldBinding( "city", String.class, true ) )
               .addFieldBinding( 3, new FieldBinding( "state", String.class, true ) )
               .addFieldBinding( 4, new FieldBinding( "zip", String.class, true ) );

        recipes.put( ArrayAddress.class, aRecipe );

        return recipes;
    }

    public List<Event<?>> events()
    {
        final ExtList<Event<?>> check = new ExtList<Event<?>>();
        check.with( new ResponseEvent( true ) );

        check.withAll( parameter( 0, getUserId(), ValueType.STRING ) );
        check.withAll( parameter( 1, getFirstName(), ValueType.STRING ) );
        check.withAll( parameter( 2, getLastName(), ValueType.STRING ) );
        check.withAll( parameter( 3, getEmail(), ValueType.STRING ) );

        final ArrayAddress addr = getAddress();
        check.with( new ParameterEvent( 4 ) );

        final ExtList<String> addrList = new ExtList<String>();
        addrList.with( addr.getLine1() )
                .with( null )
                .with( addr.getCity() )
                .with( addr.getState() )
                .with( addr.getZip() );

        check.withAll( stringArray( addrList ) );
        check.withAll( endParameter( 4, addrList, ValueType.ARRAY ) );

        check.with( new ResponseEvent( false ) );

        return check;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( address == null ) ? 0 : address.hashCode() );
        result = prime * result + ( ( email == null ) ? 0 : email.hashCode() );
        result = prime * result + ( ( firstName == null ) ? 0 : firstName.hashCode() );
        result = prime * result + ( ( lastName == null ) ? 0 : lastName.hashCode() );
        result = prime * result + ( ( userId == null ) ? 0 : userId.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final ComposedPersonResponse3 other = (ComposedPersonResponse3) obj;
        if ( address == null )
        {
            if ( other.address != null )
            {
                return false;
            }
        }
        else if ( !address.equals( other.address ) )
        {
            return false;
        }
        if ( email == null )
        {
            if ( other.email != null )
            {
                return false;
            }
        }
        else if ( !email.equals( other.email ) )
        {
            return false;
        }
        if ( firstName == null )
        {
            if ( other.firstName != null )
            {
                return false;
            }
        }
        else if ( !firstName.equals( other.firstName ) )
        {
            return false;
        }
        if ( lastName == null )
        {
            if ( other.lastName != null )
            {
                return false;
            }
        }
        else if ( !lastName.equals( other.lastName ) )
        {
            return false;
        }
        if ( userId == null )
        {
            if ( other.userId != null )
            {
                return false;
            }
        }
        else if ( !userId.equals( other.userId ) )
        {
            return false;
        }
        return true;
    }

}
