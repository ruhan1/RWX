/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.impl.stax;

import org.jdom.JDOMException;
import org.junit.BeforeClass;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

public abstract class AbstractStaxTest
{
    protected static final String DOC_PATH = "xml/";

    private static XMLInputFactory factory;

    @BeforeClass
    public static void initXmlFactory()
    {
        factory = XMLInputFactory.newInstance();
    }

    protected String getXmlPathPrefix()
    {
        return DOC_PATH;
    }

    protected void gotoElement( final XMLStreamReader reader )
        throws XMLStreamException
    {
        while ( reader.hasNext() && reader.next() != XMLStreamReader.START_ELEMENT )
        {
            //NOP
        }
    }

    protected XMLStreamReader getXML( final String name )
        throws JDOMException, IOException, XMLStreamException
    {
        final URL resource =
            Thread.currentThread().getContextClassLoader().getResource( getXmlPathPrefix() + name + ".xml" );
        return factory.createXMLStreamReader( resource.openStream() );
    }

    protected InputStream getXMLStream( final String name )
        throws JDOMException, IOException, XMLStreamException
    {
        final URL resource =
            Thread.currentThread().getContextClassLoader().getResource( getXmlPathPrefix() + name + ".xml" );
        return resource.openStream();
    }

    protected Reader getXMLReader( final String name )
        throws JDOMException, IOException, XMLStreamException
    {
        final URL resource =
            Thread.currentThread().getContextClassLoader().getResource( getXmlPathPrefix() + name + ".xml" );
        return new InputStreamReader( resource.openStream() );
    }

    protected String getXMLString( final String name )
        throws JDOMException, IOException, XMLStreamException
    {
        final URL resource =
            Thread.currentThread().getContextClassLoader().getResource( getXmlPathPrefix() + name + ".xml" );

        final BufferedReader reader = new BufferedReader( new InputStreamReader( resource.openStream() ) );
        final StringWriter writer = new StringWriter();
        final PrintWriter pWriter = new PrintWriter( writer );

        String line = null;
        while ( ( line = reader.readLine() ) != null )
        {
            pWriter.println( line );
        }

        return writer.toString();
    }

}
