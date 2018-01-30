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
package org.commonjava.rwx.core.util;

import static org.commonjava.rwx.util.ProcessorUtils.getElementClassByType;
import static org.commonjava.rwx.util.ProcessorUtils.getRegistryClassName;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 8/1/17.
 */
public class ProcessorUtilsTest
{
    @Test
    public void getRegistryNameTest()
    {
        String reg = getRegistryClassName( Collections.emptySet() );
        assertEquals( "generated._Registry", reg );

        Set<String> set = new HashSet<>(  );
        set.add( "org.apache.commons.cli" );
        set.add( "org.apache.commons.codec");

        reg = getRegistryClassName( set );
        assertEquals( "org.apache.commons.generated.Commons_Registry", reg );

        set.add( "com.yourcompany");
        reg = getRegistryClassName( set );
        assertEquals( "generated._Registry", reg );
    }

    @Test
    public void getElementClassByTypeTest()
    {
        String type = "java.util.List<java.lang.String>";
        assertEquals("java.lang.String", getElementClassByType(type));

        type = "List<java.lang.String>";
        assertEquals("java.lang.String", getElementClassByType(type));

        type = "List";
        assertEquals(null, getElementClassByType(type));
    }
}
