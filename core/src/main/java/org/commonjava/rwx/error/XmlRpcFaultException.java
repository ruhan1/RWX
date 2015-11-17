/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
