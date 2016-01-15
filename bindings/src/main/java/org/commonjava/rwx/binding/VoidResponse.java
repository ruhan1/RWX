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
package org.commonjava.rwx.binding;

import org.commonjava.rwx.binding.anno.Response;
import org.commonjava.rwx.binding.internal.xbr.XBRCompositionBindery;
import org.commonjava.rwx.binding.spi.Bindery;
import org.commonjava.rwx.error.XmlRpcFaultException;

/**
 * Placeholder to allow void method calls, while still allowing for the possibility that the response
 * will be a fault, instead of nothing at all.
 * 
 * NOTE: This object is intended to work with {@link Bindery} implementations that actually THROW
 * an {@link XmlRpcFaultException} when a fault is parsed. {@link XBRCompositionBindery} is one
 * such implementation.
 */
@Response
public class VoidResponse
{
}
