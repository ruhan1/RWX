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
package org.commonjava.rwx.testutil;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class ValueUtils
{

    private ValueUtils()
    {
    }

    public static void printByteArray( final byte[] data, final PrintWriter print )
    {
        print.println( renderByteArray( data ) );
        print.flush();
    }

    public static void printByteArray( final byte[] data, final PrintStream print )
    {
        print.println( renderByteArray( data ) );
        print.flush();
    }

    public static String renderByteArray( final byte[] data )
    {
        final StringBuilder sb = new StringBuilder();

        sb.append( data.length ).append( " bytes:\n0000: " );

        final List<Short> toPrint = new ArrayList<Short>( 16 );
        for ( int i = 1; i <= data.length; i++ )
        {
            final int uint = 0xFF & data[i - 1];
            final Short ubyte = new Short( (short) uint );
            toPrint.add( ubyte );

            final String chr = Integer.toHexString( ubyte.intValue() );
            if ( chr.length() == 1 )
            {
                sb.append( '0' );
            }

            sb.append( chr ).append( ' ' );

            if ( i > 0 )
            {
                if ( i % 16 == 0 )
                {
                    sb.append( "  " );

                    for ( final short ui : toPrint )
                    {
                        if ( ui > 32 && ui < 127 )
                        {
                            sb.append( (char) ui );
                        }
                        else
                        {
                            sb.append( '.' );
                        }
                    }
                    toPrint.clear();

                    sb.append( "\n" );

                    final String addr = Integer.toHexString( i );
                    if ( addr.length() < 4 )
                    {
                        sb.append( '0' );
                    }

                    if ( addr.length() < 3 )
                    {
                        sb.append( '0' );
                    }

                    sb.append( addr ).append( ": " );
                }
                else if ( i % 8 == 0 )
                {
                    sb.append( " " );
                }
            }
        }

        int offset = ( 16 - toPrint.size() ) * 3;
        if ( toPrint.size() > 8 )
        {
            offset += 1;
        }

        for ( int i = 0; i < offset; i++ )
        {
            sb.append( ' ' );
        }

        sb.append( "  " );

        for ( final short ui : toPrint )
        {
            if ( ui > 32 && ui < 127 )
            {
                sb.append( (char) ui );
            }
            else
            {
                sb.append( '.' );
            }
        }

        return sb.toString();
    }

}
