package org.commonjava.rwx.core;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ruhan on 7/13/17.
 */
public abstract class AbstractTest
{
    protected static final String DOC_PATH = "xml/";

    protected InputStream getXMLStream( final String name ) throws IOException, XMLStreamException
    {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream( DOC_PATH + name + ".xml" );
    }

}
