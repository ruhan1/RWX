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

package org.commonjava.rwx.binding.convert;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.value.AbstractValueBinder;
import org.commonjava.rwx.binding.spi.value.ValueUnbinder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FlexibleDateConverter
    extends AbstractValueBinder
    implements ValueUnbinder
{

    private static final String STANDARD_FORMAT = "yyyyMMdd'T'HHmmss";

    private static final String[] FORMATS =
        { STANDARD_FORMAT, "EEE MMM d HH:mm:ss z yyyy", "EEE, d MMM yyyy HH:mm:ss Z", "yyyy-MM-dd HH:mm:ss.S", };

    public FlexibleDateConverter( final Binder parent, final Class<?> type, final XBRBindingContext context )
    {
        super( parent, type, context );
    }

    @Override
    public void generate( final XmlRpcListener listener, final Object value, final Map<Class<?>, Mapping<?>> recipes )
        throws XmlRpcException
    {
        listener.value( new SimpleDateFormat( STANDARD_FORMAT ).format( value ), ValueType.DATETIME );
    }

    @Override
    public XmlRpcListener value( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( value == null || value.equals( "" ) )
        {
            return null;
        }

        final String strValue = String.valueOf( value );

        Date result = null;
        ParseException notParsable = null;
        for ( final String format : FORMATS )
        {
            try
            {
                result = new SimpleDateFormat( format ).parse( strValue );
                break;
            }
            catch ( final ParseException e )
            {
                notParsable = e;
            }
        }

        if ( notParsable != null )
        {
            throw new BindException( "Conversion failed. Cannot parse date from: " + strValue );
        }

        getParent().value( result, ValueType.DATETIME );
        return getParent();
    }

}
