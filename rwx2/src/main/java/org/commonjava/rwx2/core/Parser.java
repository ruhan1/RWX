package org.commonjava.rwx2.core;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx2.model.MethodResponse;

import javax.xml.stream.XMLStreamReader;

/**
 * Created by ruhan on 7/19/17.
 */
public interface Parser<T>
{
    //T parse( XMLStreamReader reader, ParsingContext context ) throws XmlRpcException, XmlRpcError;

    T parse( MethodResponse response );
}