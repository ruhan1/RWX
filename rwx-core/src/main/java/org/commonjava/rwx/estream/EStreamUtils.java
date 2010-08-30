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

package org.commonjava.rwx.estream;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.vocab.EventType;

import java.util.List;

public final class EStreamUtils
{

    private EStreamUtils()
    {
    }

    public static String getRequestMethod( final List<Event<?>> events )
        throws XmlRpcException
    {
        if ( events == null || events.size() < 3 || events.get( 0 ).getEventType() != EventType.START_REQUEST
            || events.get( 1 ).getEventType() != EventType.REQUEST_METHOD )
        {
            throw new XmlRpcException( "Invalid XML-RPC request." );
        }

        return ( (RequestEvent) events.get( 1 ) ).getMethod();
    }

}
