/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.vocab;

public enum EventType
{

    VALUE,

    START_STRUCT,
    START_STRUCT_MEMBER,
    END_STRUCT_MEMBER,
    STRUCT_MEMBER,
    END_STRUCT,

    START_ARRAY,
    START_ARRAY_ELEMENT,
    END_ARRAY_ELEMENT,
    ARRAY_ELEMENT,
    END_ARRAY,

    START_PARAMETER,
    END_PARAMETER,
    PARAMETER,

    START_REQUEST,
    REQUEST_METHOD,
    END_REQUEST,

    START_RESPONSE,
    FAULT,
    END_RESPONSE;

}
