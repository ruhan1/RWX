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
