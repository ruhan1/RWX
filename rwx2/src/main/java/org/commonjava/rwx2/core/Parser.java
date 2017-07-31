package org.commonjava.rwx2.core;

/**
 * Created by ruhan on 7/19/17.
 */
public interface Parser<T>
{
    T parse( Object object );
}