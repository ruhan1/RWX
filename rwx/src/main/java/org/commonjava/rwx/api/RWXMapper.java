package org.commonjava.rwx.api;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.core.Parser;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.core.Renderer;
import org.commonjava.rwx.core.XmlRpcParser;
import org.commonjava.rwx.model.Fault;
import org.commonjava.rwx.model.RpcObject;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

import static org.commonjava.rwx.util.RenderUtils.toXMLString;

/**
 * Created by ruhan on 7/12/17.
 */
public final class RWXMapper
{
    /**
     * Render an object to XML-RPC request or response string.
     *
     * @param obj
     * @return
     * @throws XmlRpcException
     */
    public String render( Object obj ) throws XmlRpcException
    {
        Renderer renderer = Registry.getInstance().getRenderer( obj.getClass() );
        if ( renderer == null )
        {
            throw new XmlRpcException( "Renderer not found for " + obj.getClass() );
        }
        Object rpcObject = renderer.render( obj );
        return toXMLString( rpcObject );
    }

    /**
     * Parse a XML-RPC request or response stream (XML string) to an object.
     *
     * @param stream
     * @param type
     * @param <T>
     * @return
     * @throws XmlRpcException
     */
    public <T> T parse( InputStream stream, Class<T> type ) throws XmlRpcException
    {
        final XmlRpcParser xmlRpcParser = new XmlRpcParser( stream );
        RpcObject rpcObject = null;
        try
        {
            rpcObject = xmlRpcParser.parse();
        }
        catch ( XMLStreamException e )
        {
            throw new XmlRpcException( "Parse to RpcObject failed", e );
        }

        if ( rpcObject instanceof Fault )
        {
            throw new XmlRpcFaultException( (Fault) rpcObject );
        }

        Parser parser = Registry.getInstance().getParser( type );
        if ( parser == null )
        {
            throw new XmlRpcException( "Parser not found for " + type );
        }
        return (T) parser.parse( rpcObject );
    }
}
