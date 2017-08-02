package org.commonjava.rwx2.test.koji;

import org.jdom.JDOMException;
import org.junit.BeforeClass;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

/**
 * Created by ruhan on 8/2/17.
 */
public abstract class AbstractTest
{
    protected static final String DOC_PATH = "xml/";

    private static XMLInputFactory factory;

    @BeforeClass
    public static void initXmlFactory()
    {
        factory = XMLInputFactory.newInstance();
    }

    protected InputStream getXMLStream( final String name ) throws IOException, XMLStreamException
    {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource( DOC_PATH + name + ".xml" );
        return resource.openStream();
    }

    protected String getXMLString( final String name ) throws JDOMException, IOException, XMLStreamException
    {
        final BufferedReader reader = new BufferedReader( new InputStreamReader( getXMLStream( name ) ) );

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        for ( ; ; )
        {
            int read = reader.read( buffer, 0, buffer.length );
            if ( read < 0 )
            {
                break;
            }
            out.append( buffer, 0, read );
        }
        return out.toString();
    }

    protected String getXMLStringIgnoreFormat( final String name ) throws JDOMException, IOException, XMLStreamException
    {
        final BufferedReader reader = new BufferedReader( new InputStreamReader( getXMLStream( name ) ) );
        final StringWriter writer = new StringWriter();
        final PrintWriter pWriter = new PrintWriter( writer );

        String line = null;
        while ( ( line = reader.readLine() ) != null )
        {
            pWriter.print( line.trim() );
        }

        return writer.toString().trim();
    }

    protected String formalizeXMLString( String xml )
    {
        xml = xml.replaceFirst( "<\\?.*\\?>", "<?xml version=\"1.0\"?>" );
        xml = xml.replaceAll( "<nil/>", "<nil></nil>" );
        return xml;
    }
}
