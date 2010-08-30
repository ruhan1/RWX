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
import org.commonjava.rwx.binding.anno.Response;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.commonjava.rwx.estream.model.StructEvent;
import org.commonjava.rwx.estream.model.ValueEvent;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.ExtMap;
import org.commonjava.rwx.vocab.EventType;
import org.commonjava.rwx.vocab.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Response
public class SimpleAddressMapResponse
    implements TestObject
{

    @DataIndex( 0 )
    @Contains( SimpleAddress.class )
    private Map<String, SimpleAddress> addresses = new ExtMap<String, SimpleAddress>( "work", new SimpleAddress() );

    public Map<String, SimpleAddress> getAddresses()
    {
        return addresses;
    }

    public void setAddresses( final Map<String, SimpleAddress> addresses )
    {
        this.addresses = addresses;
    }

    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( SimpleAddressMapResponse.class, new Integer[0] );

        recipe.addFieldBinding( 0, new FieldBinding( "addresses", Map.class ) );

        recipes.put( SimpleAddressMapResponse.class, recipe );

        recipes.putAll( new SimpleAddress().recipes() );

        return recipes;

    }

    public List<Event<?>> events()
    {
        final ExtList<Event<?>> check = new ExtList<Event<?>>( new ResponseEvent( true ) );
        check.withAll( new ParameterEvent( 0 ), new StructEvent( EventType.START_STRUCT ) );

        for ( final Map.Entry<String, SimpleAddress> entry : getAddresses().entrySet() )
        {
            check.with( new StructEvent( entry.getKey() ) );

            check.withAll( entry.getValue().events() );

            check.withAll( new ValueEvent( entry.getValue(), ValueType.STRUCT ),
                           new StructEvent( entry.getKey(), entry.getValue(), ValueType.STRUCT ),
                           new StructEvent( EventType.END_STRUCT_MEMBER ) );

        }

        check.with( new StructEvent( EventType.END_STRUCT ) );
        check.withAll( endParameter( 0, getAddresses(), ValueType.STRUCT ) );
        check.with( new ResponseEvent( false ) );

        return check;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( addresses == null ) ? 0 : addresses.hashCode() );
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
        final SimpleAddressMapResponse other = (SimpleAddressMapResponse) obj;
        if ( addresses == null )
        {
            if ( other.addresses != null )
            {
                return false;
            }
        }
        else if ( !addresses.equals( other.addresses ) )
        {
            return false;
        }
        return true;
    }

}
