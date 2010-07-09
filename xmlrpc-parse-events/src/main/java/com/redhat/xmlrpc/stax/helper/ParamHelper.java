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
import com.redhat.xmlrpc.raw.model.XmlRpcParameter;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ParamHelper
    implements StaxHelper<XmlRpcParameter<?>>
{

    private static final StructHelper STRUCT_PARSER = new StructHelper();

    private static final ArrayHelper ARRAY_PARSER = new ArrayHelper();

    private static final ValueHelper VALUE_PARSER = new ValueHelper();

    public XmlRpcParameter<?> parse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XMLStreamException, XmlRpcException
    {
        XmlRpcParameter<?> param = null;

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                final String tag = reader.getName().getLocalPart();
                if ( XmlRpcConstants.STRUCT.equals( tag ) )
                {
                    param = STRUCT_PARSER.parse( reader, null );
                }
                else if ( XmlRpcConstants.ARRAY.equals( tag ) )
                {
                    param = ARRAY_PARSER.parse( reader, null );
                }
                else if ( XmlRpcConstants.VALUE.equals( tag ) )
                {
                    param = VALUE_PARSER.parse( reader, null );
                    handler.parameter( (XmlRpcValue) param );
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }

        return param;
    }

}
