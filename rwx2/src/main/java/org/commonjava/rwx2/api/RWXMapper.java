package org.commonjava.rwx2.api;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx2.error.XmlRpcFaultException;
import org.commonjava.rwx2.core.Parser;
import org.commonjava.rwx2.core.Registry;
import org.commonjava.rwx2.core.Renderer;
import org.commonjava.rwx2.core.XmlRpcParser;
import org.commonjava.rwx2.model.Fault;
import org.commonjava.rwx2.model.RpcObject;
import org.commonjava.rwx2.util.ParseUtils;
import org.commonjava.rwx2.util.RenderUtils;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

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
        Object rpcObject = null;
        Renderer renderer = Registry.getInstance().getRenderer( obj.getClass() );
        if ( renderer != null )
        {
            rpcObject = renderer.render( obj ); // by generated x_Render class
        }
        else
        {
            rpcObject = RenderUtils.render( obj ); // by reflection
        }

        return RenderUtils.toXMLString( rpcObject );
    }

    /**
     * Parse a XML-RPC request or response stream (XML string) to an object.
     *
     * @param stream
     * @param type
     * @param <T>
     * @return
     * @throws XmlRpcException
     * @throws XMLStreamException
     */
    public <T> T parse( InputStream stream, Class<T> type ) throws XmlRpcException, XMLStreamException
    {
        final XmlRpcParser xmlRpcParser = new XmlRpcParser( stream );
        RpcObject rpcObject = xmlRpcParser.parse();

        if ( rpcObject instanceof Fault )
        {
            throw new XmlRpcFaultException( (Fault) rpcObject );
        }

        Parser parser = Registry.getInstance().getParser( type );
        if ( parser != null )
        {
            return (T) parser.parse( rpcObject ); // by generated x_Parser class
        }
        else
        {
            return ParseUtils.parse( rpcObject, type ); // by reflection
        }
    }
}
