package org.commonjava.rwx2.util;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx2.model.MethodCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.commonjava.rwx.vocab.XmlRpcConstants.ARRAY;
import static org.commonjava.rwx.vocab.XmlRpcConstants.DATA;
import static org.commonjava.rwx.vocab.XmlRpcConstants.MEMBER;
import static org.commonjava.rwx.vocab.XmlRpcConstants.METHOD_NAME;
import static org.commonjava.rwx.vocab.XmlRpcConstants.NAME;
import static org.commonjava.rwx.vocab.XmlRpcConstants.PARAM;
import static org.commonjava.rwx.vocab.XmlRpcConstants.PARAMS;
import static org.commonjava.rwx.vocab.XmlRpcConstants.REQUEST;
import static org.commonjava.rwx.vocab.XmlRpcConstants.STRUCT;
import static org.commonjava.rwx.vocab.XmlRpcConstants.VALUE;

/**
 * Created by ruhan on 7/13/17.
 */
public class RenderUtils
{
    final static Logger logger = LoggerFactory.getLogger( RenderUtils.class );

    /**
     * Render a request object to MethodCall object which represents a Map/List structure.
     *
     * @param request
     * @return
     * @throws XmlRpcException
     */
    public static MethodCall render( Object request ) throws XmlRpcException
    {
        MethodCall ret = new MethodCall();
        Class<?> cls = request.getClass();
        Request anno = cls.getAnnotation( Request.class );
        ret.setMethodName( anno.method() );
        ret.setParams( getList( request ) );
        return ret;
    }

    private static List<Object> getList( Object object ) throws XmlRpcException
    {
        Class<?> cls = object.getClass();
        Map<Integer, Object> valueMap = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for ( Field field : fields )
        {
            DataIndex dataIndex = field.getAnnotation( DataIndex.class );
            if ( dataIndex == null )
            {
                continue;
            }
            int index = dataIndex.value();

            field.setAccessible( true );
            Object value = null;
            try
            {
                value = field.get( object );
            }
            catch ( IllegalAccessException e )
            {
                logger.error( "Render error", e );
                return null;
            }

            ValueType type = ValueType.typeFor( value );
            if ( type != null )
            {
                valueMap.put( index, value ); // for primitive java types, render it immediately
            }
            else
            {
                // find the compound class annotated by ArrayPart or StructPart
                Object listOrMap = getListOrMap( value );
                valueMap.put( index, listOrMap );
            }
        }
        List<Object> params = new ArrayList<>();
        for ( int i = 0; i < valueMap.size(); i++ )
        {
            params.add( valueMap.get( i ) );
        }
        return params;
    }

    private static Object getListOrMap( Object value ) throws XmlRpcException
    {
        Class<?> vcls = value.getClass();
        ArrayPart arrayPart = vcls.getAnnotation( ArrayPart.class );
        if ( arrayPart != null )
        {
            List<Object> list = getList( value );
            return list;
        }
        else
        {
            StructPart structPart = vcls.getAnnotation( StructPart.class );
            if ( structPart != null )
            {
                Map<String, Object> map = getMap( value );
                return map;
            }
            else
            {
                throw new XmlRpcException( "No @ArrayPart or @StructPart found for " + vcls.getSimpleName() );
            }
        }
    }

    private static Map<String, Object> getMap( Object object ) throws XmlRpcException
    {
        Class<?> vcls = object.getClass();
        Map<String, Object> valueMap = new HashMap<>();
        Field[] fields = vcls.getDeclaredFields();
        for ( Field field : fields )
        {
            DataKey dataKey = field.getAnnotation( DataKey.class );
            if ( dataKey == null )
            {
                continue;
            }
            String key = dataKey.value();

            field.setAccessible( true );
            Object value = null;
            try
            {
                value = field.get( object );
            }
            catch ( IllegalAccessException e )
            {
                logger.error( "Render error", e );
                return null;
            }

            ValueType type = ValueType.typeFor( value );
            if ( type != null )
            {
                valueMap.put( key, value ); // for primitive java types, render it immediately
            }
            else
            {
                // find the compound class annotated by ArrayPart or StructPart
                Object listOrMap = getListOrMap( value );
                valueMap.put( key, listOrMap );
            }
        }
        return valueMap;
    }

    /**
     * Serialize a MethodCall object to XML string.
     *
     * @param methodCall
     * @return
     * @throws XmlRpcException
     */
    public static String toXMLString( MethodCall methodCall ) throws XmlRpcException
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
            if ( params != null && !params.isEmpty() )
            {
                w.writeStartElement( PARAMS );
                for ( Object object : params )
                {
                    w.writeStartElement( PARAM );
                    writeValue( w, object );
                    w.writeEndElement();
                }
                w.writeEndElement();
            }
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
