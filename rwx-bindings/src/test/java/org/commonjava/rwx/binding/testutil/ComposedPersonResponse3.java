/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
 */

package org.commonjava.rwx.binding.testutil;

import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.endParameter;
import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.parameter;
import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.stringArray;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Response;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.impl.estream.model.Event;
import org.commonjava.rwx.impl.estream.model.ParameterEvent;
import org.commonjava.rwx.impl.estream.model.ResponseEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.vocab.ValueType;

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

    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( ComposedPersonResponse3.class, new Integer[0] );

        recipe.addFieldBinding( 0, new FieldBinding( "userId", String.class ) )
              .addFieldBinding( 1, new FieldBinding( "firstName", String.class ) )
              .addFieldBinding( 2, new FieldBinding( "lastName", String.class ) )
              .addFieldBinding( 3, new FieldBinding( "email", String.class ) )
              .addFieldBinding( 4, new FieldBinding( "address", ArrayAddress.class ) );

        recipes.put( ComposedPersonResponse3.class, recipe );

        // ArrayAddress
        final ArrayMapping aRecipe = new ArrayMapping( ArrayAddress.class, new Integer[0] );

        aRecipe.addFieldBinding( 0, new FieldBinding( "line1", String.class ) )
               .addFieldBinding( 1, new FieldBinding( "line2", String.class ) )
               .addFieldBinding( 2, new FieldBinding( "city", String.class ) )
               .addFieldBinding( 3, new FieldBinding( "state", String.class ) )
               .addFieldBinding( 4, new FieldBinding( "zip", String.class ) );

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
