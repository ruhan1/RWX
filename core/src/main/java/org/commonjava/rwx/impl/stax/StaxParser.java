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
package org.commonjava.rwx.impl.stax;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.TrackingXmlRpcListener;
import org.commonjava.rwx.impl.stax.helper.RequestHelper;
import org.commonjava.rwx.impl.stax.helper.ResponseHelper;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.spi.XmlRpcParser;
import org.commonjava.rwx.vocab.XmlRpcConstants;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public class StaxParser
    implements XmlRpcParser
{

    private final XMLInputFactory factory;

    private XMLStreamReader reader;

    public StaxParser( final InputStream in )
        throws XmlRpcException
    {
        factory = XMLInputFactory.newInstance();
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcException( "Failed to initialize stream reader: " + e.getMessage(), e );
        }
    }

    public StaxParser( final Reader in )
        throws XmlRpcException
    {
        factory = XMLInputFactory.newInstance();
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcException( "Failed to initialize stream reader: " + e.getMessage(), e );
        }
    }

    public StaxParser( final String in )
        throws XmlRpcException
    {
        factory = XMLInputFactory.newInstance();
        try
        {
            reader = factory.createXMLStreamReader( new StringReader( in ) );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcException( "Failed to initialize stream reader: " + e.getMessage(), e );
        }
    }

    public void parse( final XmlRpcListener l )
        throws XmlRpcException
    {
        final TrackingXmlRpcListener listener = new TrackingXmlRpcListener( l );
        try
        {
            while ( reader.hasNext() )
            {
                final int tag = reader.next();
                if ( tag == XMLStreamReader.START_ELEMENT )
                {
                    final String tagName = reader.getName().getLocalPart();
                    if ( XmlRpcConstants.REQUEST.equals( tagName ) )
                    {
                        RequestHelper.parse( reader, listener );
                    }
                    else if ( XmlRpcConstants.RESPONSE.equals( tagName ) )
                    {
                        ResponseHelper.parse( reader, listener );
                    }
                }
            }
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcException( "Failed to parse input: " + e.getMessage(), e );
        }
        catch ( final RuntimeException e )
        {

        }
    }
}
