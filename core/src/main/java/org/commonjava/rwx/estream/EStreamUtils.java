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
