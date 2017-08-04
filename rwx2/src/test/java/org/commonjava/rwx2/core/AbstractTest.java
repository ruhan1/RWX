package org.commonjava.rwx2.core;

import org.apache.commons.io.IOUtils;
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
 * Created by ruhan on 7/13/17.
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
        return Thread.currentThread().getContextClassLoader().getResourceAsStream( DOC_PATH + name + ".xml" );
    }

    protected String getXMLString( final String name ) throws JDOMException, IOException, XMLStreamException
    {
        return IOUtils.toString( new InputStreamReader( getXMLStream( name ) ));
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
