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

package org.commonjava.rwx.binding.testutil;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.anno.DataIndex;

@ArrayPart
public class ArrayAddress
{

    @DataIndex( 0 )
    private String line1 = "1234 Sesame St";

    @DataIndex( 1 )
    private String line2;

    @DataIndex( 2 )
    private String city = "Walla Walla";

    @DataIndex( 3 )
    private String state = "WA";

    @DataIndex( 4 )
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
        final ArrayAddress other = (ArrayAddress) obj;
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

}
