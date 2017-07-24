package org.commonjava.rwx2.core;

import org.commonjava.rwx2.model.MethodResponse;

/**
 * Created by ruhan on 7/19/17.
 */
public interface Parser<T>
{
    T parse( MethodResponse response );
}