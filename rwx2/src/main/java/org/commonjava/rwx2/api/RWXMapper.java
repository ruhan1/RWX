package org.commonjava.rwx2.api;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx2.error.XmlRpcFaultException;
import org.commonjava.rwx2.core.Parser;
import org.commonjava.rwx2.core.Registry;
import org.commonjava.rwx2.core.Renderer;
import org.commonjava.rwx2.core.XmlRpcParser;
import org.commonjava.rwx2.model.Fault;
import org.commonjava.rwx2.model.MethodCall;
import org.commonjava.rwx2.model.MethodResponse;
import org.commonjava.rwx2.util.ParseUtils;
import org.commonjava.rwx2.util.RenderUtils;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

/**
 * Created by ruhan on 7/12/17.
 */
public final class RWXMapper
{
    public String renderRequest( Object obj ) throws XmlRpcException
    {
        MethodCall methodCall = null;
        Renderer renderer = Registry.getInstance().getRenderer( obj.getClass() );
        if ( renderer != null )
        {
            methodCall = renderer.render( obj ); // by generated x_Render class
        }
        else
        {
            methodCall = RenderUtils.render( obj ); // by reflection
        }

        return RenderUtils.toXMLString( methodCall );
    }

    public <T> T parseResponse( InputStream stream, Class<T> type ) throws XmlRpcException, XMLStreamException
    {
        final XmlRpcParser xmlRpcParser = new XmlRpcParser( stream );
        Object object = xmlRpcParser.parse();

        if ( object instanceof Fault )
        {
            throw new XmlRpcFaultException( (Fault) object );
        }

        MethodResponse response = (MethodResponse) object;

        Parser parser = Registry.getInstance().getParser( type );
        if ( parser != null )
        {
            return (T) parser.parse( response ); // by generated x_Parser class
        }
        else
        {
            return ParseUtils.parse( response, type ); // by reflection
        }
    }
}
