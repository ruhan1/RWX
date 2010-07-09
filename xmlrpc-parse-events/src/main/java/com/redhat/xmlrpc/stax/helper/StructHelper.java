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

package com.redhat.xmlrpc.stax.helper;

import com.redhat.xmlrpc.XmlRpcHandler;
import com.redhat.xmlrpc.raw.XmlRpcConstants;
import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcStruct;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StructHelper
    implements StaxHelper<XmlRpcStruct>
{

    private static final ValueHelper VALUE_PARSER = new ValueHelper();

    @Override
    public XmlRpcStruct parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        handler.startStruct();

        final XmlRpcStruct struct = new XmlRpcStruct();

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                if ( !XmlRpcConstants.MEMBER.equals( reader.getName().getLocalPart() ) )
                {
                    parseMember( reader, struct, handler );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        handler.endStruct( struct );
        return struct;
    }

    private void parseMember( final XMLStreamReader reader, final XmlRpcStruct struct, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        String name = null;
        XmlRpcValue value = null;

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                if ( XmlRpcConstants.NAME.equals( reader.getName().getLocalPart() ) )
                {
                    name = reader.getElementText().trim();
                }
                else if ( XmlRpcConstants.VALUE.equals( reader.getName().getLocalPart() ) )
                {
                    value = VALUE_PARSER.parse( reader, null );
                }
                else
                {
                    throw new XmlRpcException( "Invalid element in struct member: '" + reader.getName().getLocalPart()
                        + "'" );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        if ( name == null )
        {
            throw new XmlRpcException( "Invalid struct member. Name is missing." );
        }
        else if ( value == null )
        {
            throw new XmlRpcException( "Invalid struct member. Value is missing." );
        }

        handler.structMember( name, value );
        struct.put( name, value );
    }

}
