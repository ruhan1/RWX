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

import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.StructMapping;
import org.commonjava.rwx.estream.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.stringStruct;

@StructPart
public class SimpleAddress
        implements TestObject
{

    private String line1 = "123 Sesame St";

    private String line2;

    private String city = "Walla Walla";

    private String state = "WA";

    private String zip = "98765";

    public String getLine1()
    {
        return line1;
    }

    public void setLine1( final String line1 )
    {
        this.line1 = line1;
    }

    public String getLine2()
    {
        return line2;
    }

    public void setLine2( final String line2 )
    {
        this.line2 = line2;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( final String city )
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState( final String state )
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip( final String zip )
    {
        this.zip = zip;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( city == null ) ? 0 : city.hashCode() );
        result = prime * result + ( ( line1 == null ) ? 0 : line1.hashCode() );
        result = prime * result + ( ( line2 == null ) ? 0 : line2.hashCode() );
        result = prime * result + ( ( state == null ) ? 0 : state.hashCode() );
        result = prime * result + ( ( zip == null ) ? 0 : zip.hashCode() );
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
        final SimpleAddress other = (SimpleAddress) obj;
        if ( city == null )
        {
            if ( other.city != null )
            {
                return false;
            }
        }
        else if ( !city.equals( other.city ) )
        {
            return false;
        }
        if ( line1 == null )
        {
            if ( other.line1 != null )
            {
                return false;
            }
        }
        else if ( !line1.equals( other.line1 ) )
        {
            return false;
        }
        if ( line2 == null )
        {
            if ( other.line2 != null )
            {
                return false;
            }
        }
        else if ( !line2.equals( other.line2 ) )
        {
            return false;
        }
        if ( state == null )
        {
            if ( other.state != null )
            {
                return false;
            }
        }
        else if ( !state.equals( other.state ) )
        {
            return false;
        }
        if ( zip == null )
        {
            if ( other.zip != null )
            {
                return false;
            }
        }
        else if ( !zip.equals( other.zip ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public List<Event<?>> events()
    {
        final ArrayList<Event<?>> check = new ArrayList<Event<?>>();

        check.addAll( stringStruct( asMap() ) );

        return check;
    }

    Map<String, String> asMap()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put( "line1", getLine1() );
        map.put( "line2", getLine2() );
        map.put( "city", getCity() );
        map.put( "state", getState() );
        map.put( "zip", getZip() );
        return map;
    }

    @Override
    public Map<Class<?>, Mapping<?>> recipes()
    {
        final StructMapping sRecipe = new StructMapping( SimpleAddress.class, new String[0] );

        sRecipe.addFieldBinding( "line1", new FieldBinding( "line1", String.class ) )
               .addFieldBinding( "line2", new FieldBinding( "line2", String.class ) )
               .addFieldBinding( "city", new FieldBinding( "city", String.class ) )
               .addFieldBinding( "state", new FieldBinding( "state", String.class ) )
               .addFieldBinding( "zip", new FieldBinding( "zip", String.class ) );

        return new HashMap<Class<?>, Mapping<?>>( Collections.singletonMap( SimpleAddress.class, sRecipe ) );
    }

}
