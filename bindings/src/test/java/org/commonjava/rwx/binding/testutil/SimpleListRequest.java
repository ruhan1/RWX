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

import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.estream.model.ArrayEvent;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Request( method = "getPerson" )
public class SimpleListRequest
    implements TestObject
{

    @DataIndex( 0 )
    @Contains( String.class )
    private List<String> userIds = Arrays.asList( new String[] { "foo", "bar" } );

    public List<String> getUserIds()
    {
        return userIds;
    }

    public void setUserIds( final List<String> userIds )
    {
        this.userIds = userIds;
    }

    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( SimpleListRequest.class, new Integer[0] );
        recipe.addFieldBinding( 0, new FieldBinding( "userIds", List.class ) );
        recipes.put( SimpleListRequest.class, recipe );

        return recipes;
    }

    public List<Event<?>> events()
    {
        final List<Event<?>> check = new ArrayList<Event<?>>();

        check.addAll( Arrays.asList( new RequestEvent( true ), new RequestEvent( "getPerson" ) ) );

        check.add( new ParameterEvent( 0 ) );
        check.add( new ArrayEvent( EventType.START_ARRAY ) );

        for ( int i = 0; i < userIds.size(); i++ )
        {
            check.add( new ArrayEvent( i ) );
            check.add( new ValueEvent( userIds.get( i ), ValueType.STRING ) );
            check.add( new ArrayEvent( i, userIds.get( i ), ValueType.STRING ) );
            check.add( new ArrayEvent( EventType.END_ARRAY_ELEMENT ) );
        }

        check.add( new ArrayEvent( EventType.END_ARRAY ) );
        check.addAll( endParameter( 0, userIds, ValueType.ARRAY ) );
        check.add( new RequestEvent( false ) );

        return check;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( userIds == null ) ? 0 : userIds.hashCode() );
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
        final SimpleListRequest other = (SimpleListRequest) obj;
        if ( userIds == null )
        {
            if ( other.userIds != null )
            {
                return false;
            }
        }
        else if ( !userIds.equals( other.userIds ) )
        {
            return false;
        }
        return true;
    }

}
