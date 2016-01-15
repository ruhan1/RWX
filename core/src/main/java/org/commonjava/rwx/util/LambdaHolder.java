package org.commonjava.rwx.util;

/**
 * Created by jdcasey on 1/14/16.
 */
public class LambdaHolder<T>
{
    private T value;

    public T get()
    {
        return value;
    }

    public void set( T value )
    {
        this.value = value;
    }

    public boolean has()
    {
        return value != null;
    }
}
