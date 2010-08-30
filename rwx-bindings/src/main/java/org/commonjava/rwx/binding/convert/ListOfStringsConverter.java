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

package org.commonjava.rwx.binding.convert;

import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.binding.spi.BindingContext;
import org.commonjava.rwx.binding.spi.value.AbstractValueBinder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListOfStringsConverter
    extends AbstractValueBinder
{

    private final List<String> result = new ArrayList<String>();

    public ListOfStringsConverter( final Binder parent, final Class<?> type, final BindingContext context )
    {
        super( parent, type, context );
    }

    public ListOfStringsConverter()
    {
        super( null, null, null );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void generate( final XmlRpcListener listener, final Object value, final Map<Class<?>, Mapping<?>> recipes )
        throws XmlRpcException
    {
        final List<String> values = (List<String>) value;
        listener.startArray();

        int i = 0;
        for ( final String element : values )
        {
            listener.startArrayElement( i );
            listener.value( element, ValueType.STRING );
            listener.arrayElement( i, element, ValueType.STRING );
            listener.endArrayElement();

            i++;
        }

        listener.endArray();
        listener.value( values, ValueType.ARRAY );
    }

    @Override
    public XmlRpcListener arrayElement( final int index, final Object value, final ValueType type )
        throws XmlRpcException
    {
        for ( int i = result.size(); i <= index; i++ )
        {
            result.add( null );
        }

        result.set( index, (String) value );
        return this;
    }

    @Override
    protected Binder endArrayInternal()
        throws XmlRpcException
    {
        setValue( result, ValueType.ARRAY );
        return this;
    }

    @Override
    protected Binder startArrayInternal()
        throws XmlRpcException
    {
        result.clear();
        return this;
    }

}
