/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.xmlrpc.impl.stax;

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
