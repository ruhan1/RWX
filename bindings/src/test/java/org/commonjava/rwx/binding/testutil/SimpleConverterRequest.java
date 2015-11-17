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

import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.endParameterWithConversion;

import org.commonjava.rwx.binding.anno.Converter;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ParameterEvent;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.vocab.ValueType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Request( method = "getPerson" )
public class SimpleConverterRequest
    implements TestObject
{

    @DataIndex( 0 )
    @Converter( TestDateConverter.class )
    private Date date;

    public SimpleConverterRequest()
        throws ParseException
    {
        date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).parse( "2010-08-30 12:01:32" );
    }

    public void setDate( final Date date )
    {
        this.date = date;
    }

    public Date getDate()
    {
        return date;
    }

    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( SimpleConverterRequest.class, new Integer[0] );

        final FieldBinding binding =
            new FieldBinding( "date", Date.class ).withValueBinderType( TestDateConverter.class );

        recipe.addFieldBinding( 0, binding );
        recipes.put( SimpleConverterRequest.class, recipe );

        return recipes;
    }

    public List<Event<?>> events()
    {
        final List<Event<?>> check = new ArrayList<Event<?>>();

        check.addAll( Arrays.asList( new RequestEvent( true ), new RequestEvent( "getPerson" ) ) );

        check.add( new ParameterEvent( 0 ) );

        final String dateStr = "2010-08-30 12:01:32";
        check.addAll( endParameterWithConversion( 0, dateStr, ValueType.DATETIME, date, ValueType.DATETIME ) );
        check.add( new RequestEvent( false ) );

        return check;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( date == null ) ? 0 : date.hashCode() );
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
        final SimpleConverterRequest other = (SimpleConverterRequest) obj;
        if ( date == null )
        {
            if ( other.date != null )
            {
                return false;
            }
        }
        else if ( !date.equals( other.date ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "SimpleConverterRequest [date=" + date + "]";
    }

}
