package org.commonjava.rwx.util;

import org.commonjava.rwx.vocab.Nil;

/**
 * Created by ruhan on 7/13/17.
 */
public class ParseUtils
{
    public static Object nullifyNil( Object object )
    {
        if ( isNil( object ) )
        {
            return null;
        }
        return object;
    }

    public static boolean isNil( Object object )
    {
        return object instanceof Nil;
    }
}
