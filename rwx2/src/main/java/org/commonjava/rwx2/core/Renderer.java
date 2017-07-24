package org.commonjava.rwx2.core;

import org.commonjava.rwx2.model.MethodCall;

/**
 * Created by ruhan on 7/19/17.
 */
public interface Renderer<T>
{
    MethodCall render( T value );
}
