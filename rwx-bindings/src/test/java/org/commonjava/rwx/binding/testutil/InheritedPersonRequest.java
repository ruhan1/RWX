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

import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.vocab.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Request( method = "getUser" )
public class InheritedPersonRequest
    extends SimplePersonRequest
{
    @Override
    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( InheritedPersonRequest.class, new Integer[0] );
        recipe.addFieldBinding( 0, new FieldBinding( "userId", String.class ) );
        recipes.put( InheritedPersonRequest.class, recipe );

        return recipes;
    }

    @Override
    public List<Event<?>> events()
    {
        final ExtList<Event<?>> check = new ExtList<Event<?>>();

        check.withAll( new RequestEvent( true ), new RequestEvent( "getUser" ) );

        check.withAll( parameter( 0, "foo", ValueType.STRING ) );
        check.with( new RequestEvent( false ) );

        return check;
    }

}
