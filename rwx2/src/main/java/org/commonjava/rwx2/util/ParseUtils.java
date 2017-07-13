package org.commonjava.rwx2.util;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx2.model.MethodResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by ruhan on 7/13/17.
 */
public class ParseUtils
{
    final static Logger logger = LoggerFactory.getLogger( ParseUtils.class );

    /**
     * Parse a MethodResponse object (representing a Map/List structure) to target user object.
     *
     * @param response
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse( MethodResponse response, Class<T> type ) throws XmlRpcException
    {
        return parseList( type, response.getParams() );
    }

    private static Object parseListOrMap( Class<?> fieldType, Object value ) throws XmlRpcException
    {
        if ( value instanceof Map )
        {
            return parseMap( fieldType, (Map) value );
        }
        else if ( value instanceof List )
        {
            return parseList( fieldType, (List) value );
        }
        return null;
    }

    private static <T> T parseMap( Class<T> type, Map map ) throws XmlRpcException
    {
        T ret = null;
        try
        {
            ret = type.newInstance(); //TODO: calling constructor with proper parameters
        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "New instance error", e );
        }

        Class<?> cls = type;

        Field[] fields = cls.getDeclaredFields();
        for ( Field field : fields )
        {
            DataKey dataKey = field.getAnnotation( DataKey.class );
            if ( dataKey == null )
            {
                continue;
            }
            String key = dataKey.value();

            field.setAccessible( true );
            Object value = map.get( key );

            try
            {
                ValueType valueType = ValueType.typeFor( value );
                if ( valueType != null )
                {
                    field.set( ret, value ); // for primitive java types
                }
                else
                {
                    // for other types, find the corresponding class and set via @StructPart or @ArrayPart
                    Class<?> fieldType = field.getType();
                    Object parsed = parseListOrMap( fieldType, value );
                    field.set( ret, parsed );
                }
            }
            catch ( IllegalAccessException e )
            {
                throw new XmlRpcException( "Parse error", e );
            }
        }
        return ret;
    }

    private static <T> T parseList( Class<T> type, List<Object> list ) throws XmlRpcException
    {
        T ret = null;
        try
        {
            ret = type.newInstance(); //TODO: calling constructor with proper parameters
        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "New instance error", e );
        }

        Class<?> cls = type;

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
            Object value = list.get( index );

            try
            {
                ValueType valueType = ValueType.typeFor( value );
                if ( valueType != null )
                {
                    field.set( ret, value ); // for primitive java types
                }
                else
                {
                    // for other types, find the corresponding class and set via @StructPart or @ArrayPart
                    Class<?> fieldType = field.getType();
                    Object parsed = parseListOrMap( fieldType, value );
                    field.set( ret, parsed );
                }
            }
            catch ( IllegalAccessException e )
            {
                throw new XmlRpcException( "Parse error", e );
            }
        }
        return ret;
    }
}
