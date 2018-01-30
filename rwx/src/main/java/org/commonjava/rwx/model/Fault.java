/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.model;

/**
 * XML-RPC faults are a type of responses. If there was a problem in processing a XML-RPC request, the methodResponse
 * element will contain a fault element instead of a params element. The fault element, like the params element, has
 * only a single value that indicates something went wrong.
 *
 * Created by ruhan on 7/13/17.
 */
public final class Fault extends RpcObject
{
    private Object value;

    public Object getValue()
    {
        return value;
    }

    public void setValue( Object value )
    {
        this.value = value;
    }
}
