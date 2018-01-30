/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.rwx.test;

import org.apache.commons.io.IOUtils;
import org.commonjava.rwx.core.Registry;
import org.commonjava.rwx.test.generated.Test_Registry;
import org.junit.BeforeClass;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by ruhan on 8/2/17.
 */
public abstract class AbstractTest
{
    protected static final String DOC_PATH = "xml/";

    @BeforeClass
    public static void register()
    {
        Registry.setInstance( new Test_Registry() );
    }

    protected InputStream getXMLStream( final String name ) throws IOException, XMLStreamException
    {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream( DOC_PATH + name + ".xml" );
    }

    protected String getXMLString( final String name ) throws IOException, XMLStreamException
    {
        return IOUtils.toString( new InputStreamReader( getXMLStream( name ) ));
    }

    // Comparing XML string is a bad idea. But we need it in some cases, e.g., kojiListBuildsResponseNIL

    protected String getXMLStringIgnoreFormat( final String name ) throws IOException, XMLStreamException
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
