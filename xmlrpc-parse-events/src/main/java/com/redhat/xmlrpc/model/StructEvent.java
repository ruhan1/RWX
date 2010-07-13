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

public class StructEvent
    implements Event
{

    private final EventType eventType;

    private final String key;

    private final Object value;

    private final ValueType valueType;

    public StructEvent( final boolean start )
    {
        eventType = start ? EventType.START_STRUCT : EventType.END_STRUCT;

        key = null;
        value = null;
        valueType = null;
    }

    public StructEvent( final String key, final Object value, final ValueType valueType )
    {
        this.key = key;
        this.value = value;
        this.valueType = valueType;

        eventType = EventType.STRUCT_MEMBER;
    }

    @Override
    public EventType getEventType()
    {
        return eventType;
    }

    public String getKey()
    {
        return key;
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
