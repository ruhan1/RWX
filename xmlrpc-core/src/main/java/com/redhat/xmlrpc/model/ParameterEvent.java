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

package com.redhat.xmlrpc.model;

import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

public class ParameterEvent
    implements Event
{

    private final EventType eventType;

    private final int index;

    private final Object value;

    private final ValueType valueType;

    public ParameterEvent()
    {
        eventType = EventType.END_COMPLEX_PARAMETER;

        index = -1;
        value = null;
        valueType = null;
    }

    public ParameterEvent( final int index )
    {
        eventType = EventType.START_COMPLEX_PARAMETER;

        this.index = index;
        value = null;
        valueType = null;
    }

    public ParameterEvent( final int index, final Object value, final ValueType valueType )
    {
        this.index = index;
        this.value = value;
        this.valueType = valueType;

        eventType = EventType.PARAMETER;
    }

    @Override
    public EventType getEventType()
    {
        return eventType;
    }

    public int getIndex()
    {
        return index;
    }

    public Object getValue()
    {
        return value;
    }

    public ValueType getValueType()
    {
        return valueType;
    }

}
