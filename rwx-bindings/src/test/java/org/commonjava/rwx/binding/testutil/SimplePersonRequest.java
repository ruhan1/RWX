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

import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.parameter;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.impl.estream.model.Event;
import org.commonjava.rwx.impl.estream.model.RequestEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.vocab.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Request( method = "getPerson" )
public class SimplePersonRequest
    implements TestObject
{

    @DataIndex( 0 )
    private String userId = "foo";

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

        final ArrayMapping recipe = new ArrayMapping( SimplePersonRequest.class, new Integer[0] );
        recipe.addFieldBinding( 0, new FieldBinding( "userId", String.class ) );
        recipes.put( SimplePersonRequest.class, recipe );

        return recipes;
    }

    public List<Event<?>> events()
    {
        final ExtList<Event<?>> check = new ExtList<Event<?>>();

        check.withAll( new RequestEvent( true ), new RequestEvent( "getPerson" ) );

        check.withAll( parameter( 0, "foo", ValueType.STRING ) );
        check.with( new RequestEvent( false ) );

        return check;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
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
        final SimplePersonRequest other = (SimplePersonRequest) obj;
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
