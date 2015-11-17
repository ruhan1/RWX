/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
 */

package org.commonjava.rwx.http;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.EventStreamGenerator;
import org.commonjava.rwx.estream.model.Event;

import java.io.Closeable;
import java.util.List;

public interface SyncEStreamClient
        extends Closeable
{
    List<Event<?>> call( List<Event<?>> requestEvents, boolean expectVoidResponse )
        throws XmlRpcException;

    List<Event<?>> call( EventStreamGenerator requestGenerator, boolean expectVoidResponse )
        throws XmlRpcException;
}
