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

package com.redhat.xmlrpc.render.jdom.helper;

import org.jdom.Element;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.model.ValueType;
import com.redhat.xmlrpc.raw.XmlRpcConstants;
import com.redhat.xmlrpc.raw.model.XmlRpcArray;
import com.redhat.xmlrpc.raw.model.XmlRpcSingleValue;
import com.redhat.xmlrpc.raw.model.XmlRpcStruct;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;
import com.redhat.xmlrpc.render.error.XmlRpcRenderException;

import java.util.Map;

public class ValueHelper
    implements RendererHelper<XmlRpcValue<?>>
{

    @Override
    public Element render( final XmlRpcValue<?> value )
        throws XmlRpcException
    {
        Element result = null;

        if ( value instanceof XmlRpcArray )
        {
            result = new Element( XmlRpcConstants.ARRAY );
            final Element data = new Element( XmlRpcConstants.DATA );
            result.addContent( data );

            for ( final XmlRpcValue<?> val : ( (XmlRpcArray) value ) )
            {
                final Element v = new Element( XmlRpcConstants.VALUE );
                data.addContent( v );
                v.addContent( render( val ) );
            }
        }
        else if ( value instanceof XmlRpcStruct )
        {
            result = new Element( XmlRpcConstants.STRUCT );

            for ( final Map.Entry<String, XmlRpcValue<?>> entry : ( (XmlRpcStruct) value ).entrySet() )
            {
                final Element member = new Element( XmlRpcConstants.MEMBER );

                final Element name = new Element( XmlRpcConstants.NAME );
                name.setText( entry.getKey() );
                member.addContent( name );

                final Element val = new Element( XmlRpcConstants.VALUE );
                member.addContent( val );

                final Element rendered = render( entry.getValue() );
                if ( XmlRpcConstants.VALUE.equals( rendered.getName() ) )
                {
                    val.addContent( rendered.getChildren() );
                }
                else
                {
                    val.addContent( rendered );
                }
            }
        }
        else if ( value instanceof XmlRpcSingleValue )
        {
            final Element wrapper = new Element( XmlRpcConstants.VALUE );

            final XmlRpcSingleValue singleValue = ( (XmlRpcSingleValue) value );

            final ValueType vt = singleValue.getType();
            final Element val = new Element( vt.getPrimaryTag() );
            wrapper.addContent( val );

            val.setText( vt.coercion().toString( singleValue.getValue() ) );
        }
        else
        {
            throw new XmlRpcRenderException( "Invalid value type: " + value.getClass().getName() + " for: " + value );
        }

        return result;
    }

}
