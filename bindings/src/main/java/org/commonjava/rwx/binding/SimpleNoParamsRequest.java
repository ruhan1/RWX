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
package org.commonjava.rwx.binding;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcGenerator;
import org.commonjava.rwx.spi.XmlRpcListener;

public class SimpleNoParamsRequest
    implements XmlRpcGenerator
{

    private final String methodName;

    public SimpleNoParamsRequest( final String methodName )
    {
        this.methodName = methodName;
    }

    @Override
    public XmlRpcGenerator generate( final XmlRpcListener listener )
        throws XmlRpcException
    {
        listener.startRequest().requestMethod( methodName ).endRequest();
        return this;
    }

    public String getMethodName()
    {
        return methodName;
    }

}
