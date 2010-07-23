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

import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.mapping.discovery.Mapper;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListOfStringsConverter
    extends AbstractXmlRpcListener
    implements ValueConverter
{

    private XmlRpcListener parent;

    private final List<String> result = new ArrayList<String>();

    public Map<Class<?>, Mapping<?>> getSupplementalRecipes( final Mapper loader )
    {
        return Collections.emptyMap();
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
    }

    @Override
    public void setContext( final XmlRpcListener parent, final Map<Class<?>, Mapping<?>> recipes )
    {
        this.parent = parent;
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
    public XmlRpcListener endArray()
        throws XmlRpcException
    {
        parent.value( result, ValueType.ARRAY );
        return parent;
    }

    @Override
    public XmlRpcListener startArray()
        throws XmlRpcException
    {
        result.clear();
        return this;
    }

}
