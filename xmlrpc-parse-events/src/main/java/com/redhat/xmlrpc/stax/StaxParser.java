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

package com.redhat.xmlrpc.stax;

import com.redhat.xmlrpc.XmlRpcHandler;
import com.redhat.xmlrpc.error.XmlRpcConfigurationException;
import com.redhat.xmlrpc.raw.XmlRpcConstants;
import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcRequest;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;
import com.redhat.xmlrpc.spi.NoOpXmlRpcHandler;
import com.redhat.xmlrpc.spi.XmlRpcParser;
import com.redhat.xmlrpc.stax.helper.RequestHelper;
import com.redhat.xmlrpc.stax.helper.ResponseHelper;

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

    public StaxParser()
    {
        factory = XMLInputFactory.newInstance();
    }

    public XmlRpcRequest parseRequest( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        try
        {
            while ( reader.hasNext() )
            {
                final int tag = reader.next();
                if ( tag == XMLStreamReader.START_ELEMENT )
                {
                    final String tagName = reader.getName().getLocalPart();
                    if ( !XmlRpcConstants.REQUEST.equals( tagName ) )
                    {
                        throw new XmlRpcException( "Expected document element: '" + XmlRpcConstants.REQUEST
                            + "', got: '" + tagName + "'." );
                    }

                    return new RequestHelper().parse( reader, handler );
                }
            }
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcException( "Failed to parse input: " + e.getMessage(), e );
        }

        return null;
    }

    public XmlRpcResponse parseResponse( final XMLStreamReader reader, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        try
        {
            while ( reader.hasNext() )
            {
                final int tag = reader.next();
                if ( tag == XMLStreamReader.START_ELEMENT )
                {
                    final String tagName = reader.getName().getLocalPart();
                    if ( !XmlRpcConstants.RESPONSE.equals( tagName ) )
                    {
                        throw new XmlRpcException( "Expected document element: '" + XmlRpcConstants.RESPONSE
                            + "', got: '" + tagName + "'." );
                    }

                    return new ResponseHelper().parse( reader, handler );
                }
            }
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcException( "Failed to parse input: " + e.getMessage(), e );
        }

        return null;
    }

    @Override
    public XmlRpcRequest parseRequest( final InputStream in )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseRequest( reader, new NoOpXmlRpcHandler() );
    }

    @Override
    public XmlRpcRequest parseRequest( final Reader in )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseRequest( reader, new NoOpXmlRpcHandler() );
    }

    @Override
    public XmlRpcRequest parseRequest( final String in )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( new StringReader( in ) );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseRequest( reader, new NoOpXmlRpcHandler() );
    }

    @Override
    public XmlRpcResponse parseResponse( final InputStream in )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseResponse( reader, new NoOpXmlRpcHandler() );
    }

    @Override
    public XmlRpcResponse parseResponse( final Reader in )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseResponse( reader, new NoOpXmlRpcHandler() );
    }

    @Override
    public XmlRpcResponse parseResponse( final String in )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( new StringReader( in ) );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseResponse( reader, new NoOpXmlRpcHandler() );
    }

    @Override
    public XmlRpcRequest parseRequest( final InputStream in, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseRequest( reader, handler );
    }

    @Override
    public XmlRpcRequest parseRequest( final Reader in, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseRequest( reader, handler );
    }

    @Override
    public XmlRpcRequest parseRequest( final String xml, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( new StringReader( xml ) );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseRequest( reader, handler );
    }

    @Override
    public XmlRpcResponse parseResponse( final InputStream in, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseResponse( reader, handler );
    }

    @Override
    public XmlRpcResponse parseResponse( final Reader in, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( in );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseResponse( reader, handler );
    }

    @Override
    public XmlRpcResponse parseResponse( final String xml, final XmlRpcHandler handler )
        throws XmlRpcException
    {
        XMLStreamReader reader;
        try
        {
            reader = factory.createXMLStreamReader( new StringReader( xml ) );
        }
        catch ( final XMLStreamException e )
        {
            throw new XmlRpcConfigurationException( "Failed to create stream reader: " + e.getMessage(), e );
        }

        return parseResponse( reader, handler );
    }

}
