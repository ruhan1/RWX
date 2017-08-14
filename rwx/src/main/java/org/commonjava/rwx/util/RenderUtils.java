package org.commonjava.rwx.util;

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx.model.MethodCall;
import org.commonjava.rwx.model.MethodResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.commonjava.rwx.vocab.XmlRpcConstants.*;

/**
 * Created by ruhan on 7/13/17.
 */
public class RenderUtils
{
    final static Logger logger = LoggerFactory.getLogger( RenderUtils.class );

    /**
     * Serialize a MethodCall or MethodResponse object to XML string.
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
        else
        {
            throw new XmlRpcException( "Not supported, " + rpcObject.getClass() );
        }
    }

    private static String toResponseXMLString( MethodResponse methodResponse ) throws XmlRpcException
    {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        StringWriter result = new StringWriter();
        try
        {
            XMLStreamWriter w = output.createXMLStreamWriter( result );
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
            logger.error( "Write to XML error", e );
            return null;
        }

        return result.toString();
    }

    private static String toRequestXMLString( MethodCall methodCall ) throws XmlRpcException
    {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        StringWriter result = new StringWriter();
        try
        {
            XMLStreamWriter w = output.createXMLStreamWriter( result );
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
            logger.error( "Write to XML error", e );
            return null;
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
    }

    private static void writeArray( XMLStreamWriter w, List<Object> objects )
                    throws XMLStreamException, CoercionException
    {
        w.writeStartElement( VALUE );
        w.writeStartElement( ARRAY );
        w.writeStartElement( DATA );
        for ( Object object : objects )
        {
            writeValue( w, object );
        }
        w.writeEndElement();
        w.writeEndElement();
        w.writeEndElement();
    }

    private static void writeStruct( XMLStreamWriter w, Map<String, Object> map )
                    throws XMLStreamException, CoercionException
    {
        w.writeStartElement( VALUE );
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
        w.writeEndElement();
    }

    private static void writePrimitive( XMLStreamWriter w, Object object ) throws XMLStreamException, CoercionException
    {
        ValueType type = ValueType.safeTypeFor( object );

        w.writeStartElement( VALUE );
        w.writeStartElement( type.getPrimaryTag() );
        w.writeCharacters( type.coercion().toString( object ) );
        w.writeEndElement();
        w.writeEndElement();
    }

}
