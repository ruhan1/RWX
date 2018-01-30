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
package org.commonjava.rwx.util;

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx.model.MethodCall;
import org.commonjava.rwx.model.MethodResponse;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.commonjava.rwx.vocab.XmlRpcConstants.*;

/**
 * Created by ruhan on 7/13/17.
 */
public class RenderUtils
{
    private static XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    /**
     * Serialize a MethodCall, MethodResponse, Map, or List object to XML string.
     *
     * @param rpcObject
     * @return
     * @throws XmlRpcException
     */
    public static String toXMLString( Object rpcObject ) throws XmlRpcException
    {
        if ( rpcObject instanceof MethodCall )
        {
            return toRequestXMLString( (MethodCall) rpcObject );
        }
        else if ( rpcObject instanceof MethodResponse )
        {
            return toResponseXMLString( (MethodResponse) rpcObject );
        }
        else if ( rpcObject instanceof Map )
        {
            return toStructPartXMLString( (Map) rpcObject );
        }
        else if ( rpcObject instanceof List )
        {
            return toArrayPartXMLString( (List) rpcObject );
        }
        else
        {
            throw new XmlRpcException( "Not supported, " + rpcObject.getClass() );
        }
    }

    private static String toStructPartXMLString( Map rpcObject ) throws XmlRpcException
    {
        StringWriter result = new StringWriter();
        try
        {
            XMLStreamWriter w = outputFactory.createXMLStreamWriter( result );
            writeStruct( w, rpcObject );
            w.close();
        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "toArrayPartXMLString error", e );
        }
        return result.toString();
    }

    private static String toArrayPartXMLString( List rpcObject ) throws XmlRpcException
    {
        StringWriter result = new StringWriter();
        try
        {
            XMLStreamWriter w = outputFactory.createXMLStreamWriter( result );
            writeArray( w, rpcObject );
            w.close();
        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "toArrayPartXMLString error", e );
        }
        return result.toString();
    }

    private static String toResponseXMLString( MethodResponse methodResponse ) throws XmlRpcException
    {
        StringWriter result = new StringWriter();
        try
        {
            XMLStreamWriter w = outputFactory.createXMLStreamWriter( result );
            w.writeStartDocument();
            w.writeStartElement( RESPONSE );
            List<Object> params = methodResponse.getParams();
            writeParams( w, params );
            w.writeEndElement();
            w.writeEndDocument();
            w.close();
        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "toResponseXMLString error", e );
        }

        return result.toString();
    }

    private static String toRequestXMLString( MethodCall methodCall ) throws XmlRpcException
    {
        StringWriter result = new StringWriter();
        try
        {
            XMLStreamWriter w = outputFactory.createXMLStreamWriter( result );
            w.writeStartDocument();
            w.writeStartElement( REQUEST );
            w.writeStartElement( METHOD_NAME );
            w.writeCharacters( methodCall.getMethodName() );
            w.writeEndElement();
            List<Object> params = methodCall.getParams();
            writeParams( w, params );
            w.writeEndElement();
            w.writeEndDocument();
            w.close();
        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "toRequestXMLString error", e );
        }

        return result.toString();
    }

    private static void writeParams( XMLStreamWriter w, List<Object> params ) throws XmlRpcException, XMLStreamException
    {
        w.writeStartElement( PARAMS );
        if ( params != null && !params.isEmpty() )
        {
            for ( Object object : params )
            {
                w.writeStartElement( PARAM );
                writeValue( w, object );
                w.writeEndElement();
            }
        }
        w.writeEndElement();
    }

    private static void writeValue( XMLStreamWriter w, Object object ) throws XMLStreamException, CoercionException
    {
        w.writeStartElement( VALUE );

        if ( object instanceof List )
        {
            writeArray( w, (List) object );
        }
        else if ( object instanceof Map )
        {
            writeStruct( w, (Map) object );
        }
        else
        {
            writePrimitive( w, object );
        }

        w.writeEndElement();
    }

    private static void writeArray( XMLStreamWriter w, List<Object> objects )
                    throws XMLStreamException, CoercionException
    {
        w.writeStartElement( ARRAY );
        w.writeStartElement( DATA );
        for ( Object object : objects )
        {
            writeValue( w, object );
        }
        w.writeEndElement();
        w.writeEndElement();
    }

    private static void writeStruct( XMLStreamWriter w, Map<String, Object> map )
                    throws XMLStreamException, CoercionException
    {
        w.writeStartElement( STRUCT );
        for ( Map.Entry<String, Object> entry : map.entrySet() )
        {
            w.writeStartElement( MEMBER );
            w.writeStartElement( NAME );
            w.writeCharacters( entry.getKey() );
            w.writeEndElement();
            writeValue( w, entry.getValue() );
            w.writeEndElement();
        }
        w.writeEndElement();
    }

    private static void writePrimitive( XMLStreamWriter w, Object object ) throws XMLStreamException, CoercionException
    {
        ValueType type = ValueType.safeTypeFor( object );

        w.writeStartElement( type.getPrimaryTag() );
        String chars = type.coercion().toString( object );
        if ( isNotBlank( chars ) )
        {
            w.writeCharacters( chars );
        }
        w.writeEndElement();
    }

}
