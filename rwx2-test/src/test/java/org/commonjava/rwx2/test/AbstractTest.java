package org.commonjava.rwx2.test;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // the members in struct is not ordered. we have to make an ordered list to compare xml files
    protected String splitAndSort( String xml )
    {
        String MEMBER = "<member>";
        String MEMBER_END = "</member>";

        StringBuilder sb = new StringBuilder();

        List<String> membersList = new ArrayList<>();

        String newSource = xml.replaceAll( MEMBER, "\n" + MEMBER );
        String[] lines = newSource.split( "\n" );
        for ( String line : lines )
        {
            if ( line.startsWith( MEMBER ) )
            {
                if ( line.endsWith( MEMBER_END ) )
                {
                    membersList.add( line );
                }
                else
                {
                    // get the last member in a struct
                    int idx = line.indexOf( MEMBER_END );
                    String m = line.substring( 0, idx + MEMBER_END.length() );
                    membersList.add( m );
                    String[] array = membersList.toArray( new String[0] );
                    Arrays.sort( array );
                    for ( String s : array )
                    {
                        sb.append( s + "\n" );
                    }
                    membersList = new ArrayList<>();
                    String following = line.substring( idx + MEMBER_END.length() ); // what is following </member>...
                    sb.append( following + "\n" );
                }
            }
            else
            {
                sb.append( line + "\n" );
            }
        }

        return sb.toString();
    }

}
