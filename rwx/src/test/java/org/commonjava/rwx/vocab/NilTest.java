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
package org.commonjava.rwx.vocab;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class NilTest
{
    @Test
    public void serializationTest() throws IOException, ClassNotFoundException
    {
        Object result = null;

        try ( ByteArrayOutputStream bout = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream( bout ) )
        {
            out.writeObject( Nil.NIL_VALUE );

            out.close();

            try ( ByteArrayInputStream bin = new ByteArrayInputStream( bout.toByteArray() ); ObjectInputStream in = new ObjectInputStream( bin ) )
            {
                result = in.readObject();
            }
        }

        assertEquals( Nil.NIL_VALUE, result );

        assertEquals( result, Nil.NIL_VALUE );

        assertTrue( Nil.NIL_VALUE == result );
    }
}
