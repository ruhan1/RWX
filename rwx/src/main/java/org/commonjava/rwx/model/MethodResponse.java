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
 * Responses are much like requests. If the response is successful - the procedure was found,
 * executed correctly, and returned results - then the XML-RPC response will look much like a request,
 * except that the methodCall element is replaced by a methodResponse and there is no methodName.
 *
 * Responses follow some additional constraints:
 *
 * 1. An XML-RPC response can only contain one parameter.
 * 2. That parameter may be an array or a struct, so it is possible to return multiple values.
 * 3. It is always required to return a value in response. A "success value" - perhaps a Boolean set to true (1).
 *
 * Created by ruhan on 7/13/17.
 */
public final class MethodResponse
                extends RpcObject
{
}
