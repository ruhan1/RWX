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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/**
 * Created by ruhan on 12/14/17.
 */
public class Nil
    implements Serializable
{
    private static final long serialVersionUID = 5398616344106741077L;

    private static final int VERSION = 1;

    public static final Nil NIL_VALUE = new Nil();

   private Nil()
   {

   }

    @Override
    public int hashCode()
    {
        return 31;
    }

    @Override
    public boolean equals( Object obj )
    {
        return ( obj instanceof Nil );
    }

    private Object writeReplace()
    {
        return new Proxy();
    }

    private static class Proxy
        implements Externalizable
    {
        public Proxy()
        {

        }

        @Override
        public void writeExternal( ObjectOutput out )
                throws IOException {
            out.writeInt( VERSION );
        }

        @Override
        public void readExternal( ObjectInput in )
                throws IOException {
            int version = in.readInt();

            if ( version != 1 )
            {
                throw new IOException( "Invalid version: " + version );
            }
        }

        private Object readResolve()
        {
            return NIL_VALUE;
        }
    }
}
