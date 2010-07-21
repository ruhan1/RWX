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

package org.commonjava.rwx.error;

public class XmlRpcFaultException
    extends XmlRpcException
{

    private static final long serialVersionUID = 1L;

    private final int code;

    private final String faultMessage;

    public XmlRpcFaultException( final int code, final String message )
    {
        super( code + ": " + message );
        this.code = code;
        faultMessage = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getReason()
    {
        return faultMessage;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        result = prime * result + ( ( faultMessage == null ) ? 0 : faultMessage.hashCode() );
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
        final XmlRpcFaultException other = (XmlRpcFaultException) obj;
        if ( code != other.code )
        {
            return false;
        }
        if ( faultMessage == null )
        {
            if ( other.faultMessage != null )
            {
                return false;
            }
        }
        else if ( !faultMessage.equals( other.faultMessage ) )
        {
            return false;
        }
        return true;
    }

}
