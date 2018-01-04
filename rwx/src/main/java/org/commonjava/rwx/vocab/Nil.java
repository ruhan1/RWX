package org.commonjava.rwx.vocab;

/**
 * Created by ruhan on 12/14/17.
 */
public class Nil
{
    public static Nil NIL_VALUE = new Nil();

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
}
