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

package com.redhat.xmlrpc.impl.stax.helper;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.spi.AbstractXmlRpcListener;
import com.redhat.xmlrpc.spi.XmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;
import com.redhat.xmlrpc.vocab.XmlRpcConstants;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class FaultHelper
    implements StaxHelper
{

    private static final StructHelper STRUCT_HELPER = new StructHelper();

    @Override
    public void parse( final XMLStreamReader reader, final XmlRpcListener handler )
        throws XMLStreamException, XmlRpcException
    {
        boolean fired = false;

        int level = 1;
        while ( reader.hasNext() && level > 0 )
        {
            final int type = reader.next();
            if ( type == XMLStreamReader.START_ELEMENT )
            {
                level++;

                if ( !fired )
                {
                    final String tag = reader.getName().getLocalPart();
                    if ( XmlRpcConstants.STRUCT.equals( tag ) )
                    {
                        final FaultListener fl = new FaultListener();
                        STRUCT_HELPER.parse( reader, fl );

                        handler.fault( fl.getCode(), fl.getMessage() );
                    }
                    fired = true;
                }
            }
            else if ( type == XMLStreamReader.END_ELEMENT )
            {
                level--;
            }
        }
    }

    private static final class FaultListener
        extends AbstractXmlRpcListener
    {
        private int code;

        private String message;

        int getCode()
        {
            return code;
        }

        String getMessage()
        {
            return message;
        }

        @Override
        public void structMember( final String key, final Object value, final ValueType type )
        {
            if ( XmlRpcConstants.FAULT_CODE.equals( key ) )
            {
                code = (Integer) value;
            }
            else if ( XmlRpcConstants.FAULT_STRING.equals( key ) )
            {
                message = (String) value;
            }
        }

    }

}
