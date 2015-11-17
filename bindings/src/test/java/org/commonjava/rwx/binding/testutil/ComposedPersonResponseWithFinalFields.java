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
import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.stringStruct;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.IndexRefs;
import org.commonjava.rwx.binding.anno.Response;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.StructMapping;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Response
public class ComposedPersonResponseWithFinalFields
    implements TestObject
{

    @DataIndex( 0 )
    private final String userId;

    @DataIndex( 1 )
    private String firstName;

    @DataIndex( 2 )
    private String lastName;

    @DataIndex( 3 )
    private final String email;

    @DataIndex( 4 )
    private final SimpleFinalFieldAddress address;

    @IndexRefs( { 0, 3, 4 } )
    public ComposedPersonResponseWithFinalFields( final String userId, final String email,
                                                  final SimpleFinalFieldAddress address )
    {
        this.userId = userId;
        this.email = email;
        this.address = address;
    }

    public SimpleFinalFieldAddress getAddress()
    {
        return address;
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

    public String getUserId()
    {
        return userId;
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
        final ComposedPersonResponseWithFinalFields other = (ComposedPersonResponseWithFinalFields) obj;
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

    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( ComposedPersonResponseWithFinalFields.class, 0, 3, 4 );

        recipe.addFieldBinding( 0, new FieldBinding( "userId", String.class ) )
              .addFieldBinding( 1, new FieldBinding( "firstName", String.class ) )
              .addFieldBinding( 2, new FieldBinding( "lastName", String.class ) )
              .addFieldBinding( 3, new FieldBinding( "email", String.class ) )
              .addFieldBinding( 4, new FieldBinding( "address", SimpleFinalFieldAddress.class ) );

        recipes.put( ComposedPersonResponseWithFinalFields.class, recipe );

        // SimpleAddress
        final StructMapping sRecipe =
            new StructMapping( SimpleFinalFieldAddress.class, "line1", "city", "state", "zip" );

        sRecipe.addFieldBinding( "line1", new FieldBinding( "line1", String.class ) )
               .addFieldBinding( "line2", new FieldBinding( "line2", String.class ) )
               .addFieldBinding( "city", new FieldBinding( "city", String.class ) )
               .addFieldBinding( "state", new FieldBinding( "state", String.class ) )
               .addFieldBinding( "zip", new FieldBinding( "zip", String.class ) );

        recipes.put( SimpleFinalFieldAddress.class, sRecipe );

        return recipes;

    }

    public List<Event<?>> events()
    {
        final ArrayList<Event<?>> check = new ArrayList<Event<?>>();

        check.add( new ResponseEvent( true ) );

        check.addAll( parameter( 0, getUserId(), ValueType.STRING ) );
        check.addAll( parameter( 1, getFirstName(), ValueType.STRING ) );
        check.addAll( parameter( 2, getLastName(), ValueType.STRING ) );
        check.addAll( parameter( 3, getEmail(), ValueType.STRING ) );

        final SimpleFinalFieldAddress addr = getAddress();
        check.add( new ParameterEvent( 4 ) );

        final HashMap<String, String> addrMap = new HashMap<String, String>();
        addrMap.put( "line1", addr.getLine1() );
        addrMap.put( "line2", null );
        addrMap.put( "city", addr.getCity() );
        addrMap.put( "state", addr.getState() );
        addrMap.put( "zip", addr.getZip() );

        check.addAll( stringStruct( addrMap ) );
        check.addAll( endParameter( 4, addrMap, ValueType.STRUCT ) );

        check.add( new ResponseEvent( false ) );

        return check;
    }

    @Override
    public String toString()
    {
        return "ComposedPersonResponseWithFinalFields [address=" + address + "\n email=" + email + "\n firstName="
            + firstName + "\n lastName=" + lastName + "\n userId=" + userId + "]";
    }

}
