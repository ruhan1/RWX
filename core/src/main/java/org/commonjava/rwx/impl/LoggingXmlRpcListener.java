package org.commonjava.rwx.impl;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jdcasey on 1/8/16.
 */
public class LoggingXmlRpcListener
    implements XmlRpcListener
{
    private final Logger logger = LoggerFactory.getLogger( getClass() );

    private XmlRpcListener delegate;

    public LoggingXmlRpcListener( XmlRpcListener delegate )
    {
        this.delegate = delegate;
    }

    @Override
    public XmlRpcListener value( Object value, ValueType type )
            throws XmlRpcException
    {
        logger.info( "value: {} (type: {}, class: {})", value, type, value == null ? "NULL" : value.getClass().getName() );
        return delegate.value( value, type );
    }

    @Override
    public XmlRpcListener fault( int code, String message )
            throws XmlRpcException
    {
        logger.info( "fault: {} (code: {})", message, code );
        return delegate.fault( code, message );
    }

    @Override
    public XmlRpcListener startRequest()
            throws XmlRpcException
    {
        logger.info( "request started" );
        return delegate.startRequest();
    }

    @Override
    public XmlRpcListener requestMethod( String methodName )
            throws XmlRpcException
    {
        logger.info( "request method: {}", methodName );
        return delegate.requestMethod( methodName );
    }

    @Override
    public XmlRpcListener endRequest()
            throws XmlRpcException
    {
        logger.info( "request ended" );
        return delegate.endRequest();
    }

    @Override
    public XmlRpcListener startResponse()
            throws XmlRpcException
    {
        logger.info( "response started" );
        return delegate.startResponse();
    }

    @Override
    public XmlRpcListener endResponse()
            throws XmlRpcException
    {
        logger.info( "response ended" );
        return delegate.endResponse();
    }

    @Override
    public XmlRpcListener startParameter( int index )
            throws XmlRpcException
    {
        logger.info( "parameter {} start", index );
        return delegate.startParameter( index );
    }

    @Override
    public XmlRpcListener endParameter()
            throws XmlRpcException
    {
        logger.info( "parameter ended" );
        return delegate.endParameter();
    }

    @Override
    public XmlRpcListener parameter( int index, Object value, ValueType type )
            throws XmlRpcException
    {
        logger.info( "parameter {} of type: {} is: {} (class: {})", index, type, value, value == null ? "NULL" : value.getClass().getName() );
        return delegate.parameter( index, value, type );
    }

    @Override
    public XmlRpcListener startArray()
            throws XmlRpcException
    {
        logger.info( "array started" );
        return delegate.startArray();
    }

    @Override
    public XmlRpcListener startArrayElement( int index )
            throws XmlRpcException
    {
        logger.info( "started array element {}", index );
        return delegate.startArrayElement( index );
    }

    @Override
    public XmlRpcListener endArrayElement()
            throws XmlRpcException
    {
        logger.info( "ended array element" );
        return delegate.endArrayElement();
    }

    @Override
    public XmlRpcListener arrayElement( int index, Object value, ValueType type )
            throws XmlRpcException
    {
        logger.info( "array element {} of type: {} is: {} (class: {})", index, type, value, value == null ? "NULL" : value.getClass().getName() );
        return delegate.arrayElement( index, value, type );
    }

    @Override
    public XmlRpcListener endArray()
            throws XmlRpcException
    {
        logger.info( "ended array" );
        return delegate.endArray();
    }

    @Override
    public XmlRpcListener startStruct()
            throws XmlRpcException
    {
        logger.info( "started struct" );
        return delegate.startStruct();
    }

    @Override
    public XmlRpcListener startStructMember( String key )
            throws XmlRpcException
    {
        logger.info( "started struct member: {}", key );
        return delegate.startStructMember( key );
    }

    @Override
    public XmlRpcListener endStructMember()
            throws XmlRpcException
    {
        logger.info( "ended struct member" );
        return delegate.endStructMember();
    }

    @Override
    public XmlRpcListener structMember( String key, Object value, ValueType type )
            throws XmlRpcException
    {
        logger.info( "struct member {} of type: {} is: {} (class: {})", key, type, value, value == null ? "NULL" : value.getClass().getName() );
        return delegate.structMember( key, value, type );
    }

    @Override
    public XmlRpcListener endStruct()
            throws XmlRpcException
    {
        logger.info( "ended struct" );
        return delegate.endStruct();
    }
}
