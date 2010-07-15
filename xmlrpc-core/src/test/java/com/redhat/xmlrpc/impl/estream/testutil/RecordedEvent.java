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

package com.redhat.xmlrpc.impl.estream.testutil;

import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

public final class RecordedEvent
{

    private final EventType eventType;

    private final Object key;

    private final Object value;

    private final ValueType valueType;

    public RecordedEvent( final EventType eventType, final Object key, final Object value, final ValueType valueType )
    {
        this.eventType = eventType;
        this.key = key;
        this.value = value;
        this.valueType = valueType;
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public Object getKey()
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

    @Override
    public String toString()
    {
        return "RecordedEvent [eventType=" + eventType + ", key=" + key + ", value=" + value + ", valueType="
            + valueType + "]";
    }

}
