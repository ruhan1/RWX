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

package com.redhat.xmlrpc.impl.estream;

import com.redhat.xmlrpc.model.ArrayEvent;
import com.redhat.xmlrpc.model.Event;
import com.redhat.xmlrpc.model.ParameterEvent;
import com.redhat.xmlrpc.model.RequestEvent;
import com.redhat.xmlrpc.model.ResponseEvent;
import com.redhat.xmlrpc.model.StructEvent;
import com.redhat.xmlrpc.spi.XmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;

import java.util.ArrayList;
import java.util.List;

public class EventStreamParser
    implements XmlRpcListener
{

    private final List<Event> events = new ArrayList<Event>();

    public List<Event> getEvents()
    {
        return events;
    }

    @Override
    public void arrayElement( final int index, final Object value, final ValueType type )
    {
        events.add( new ArrayEvent( index, value, type ) );
    }

    @Override
    public void endArray()
    {
        events.add( new ArrayEvent( false ) );
    }

    @Override
    public void endComplexParameter()
    {
        events.add( new ParameterEvent() );
    }

    @Override
    public void endRequest()
    {
        events.add( new RequestEvent( false ) );
    }

    @Override
    public void endResponse()
    {
        events.add( new ResponseEvent( false ) );
    }

    @Override
    public void endStruct()
    {
        events.add( new StructEvent( false ) );
    }

    @Override
    public void fault( final int code, final String message )
    {
        events.add( new ResponseEvent( code, message ) );
    }

    @Override
    public void parameter( final int index, final Object value, final ValueType type )
    {
        events.add( new ParameterEvent( index, value, type ) );
    }

    @Override
    public void requestMethod( final String methodName )
    {
        events.add( new RequestEvent( methodName ) );
    }

    @Override
    public void startArray()
    {
        events.add( new ArrayEvent( true ) );
    }

    @Override
    public void startComplexParameter( final int index )
    {
        events.add( new ParameterEvent( index ) );
    }

    @Override
    public void startRequest()
    {
        events.add( new RequestEvent( true ) );
    }

    @Override
    public void startResponse()
    {
        events.add( new ResponseEvent( true ) );
    }

    @Override
    public void startStruct()
    {
        events.add( new StructEvent( true ) );
    }

    @Override
    public void structMember( final String key, final Object value, final ValueType type )
    {
        events.add( new StructEvent( key, value, type ) );
    }

}
