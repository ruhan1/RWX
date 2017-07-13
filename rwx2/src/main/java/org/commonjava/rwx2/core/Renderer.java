package org.commonjava.rwx2.core;

import org.commonjava.rwx2.model.MethodCall;

import javax.xml.stream.XMLStreamWriter;

/**
 * Created by ruhan on 7/19/17.
 */
public interface Renderer<T>
{
    //void render( XMLStreamWriter writer, RenderingContext context, T value);

    MethodCall render( T value );
}
