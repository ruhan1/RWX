/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
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
