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

package org.commonjava.rwx.impl.jdom;

import static org.junit.Assert.assertEquals;

import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.impl.estream.testutil.ExtList;
import org.commonjava.rwx.impl.estream.testutil.ExtMap;
import org.commonjava.rwx.impl.jdom.JDomRenderer;
import org.commonjava.rwx.vocab.ValueType;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;


import java.io.IOException;
import java.net.URL;

public class JDomRendererTest
{

    private static final String DOC_PATH = "xml/";

    private final SAXBuilder builder = new SAXBuilder();

    @Test
    public void simpleRequest()
        throws JDOMException, IOException, XmlRpcException
    {
        assertDocument( "simpleRequest", (JDomRenderer) new JDomRenderer().startRequest()
                                                                          .requestMethod( "foo" )
                                                                          .endRequest() );
    }

    @Test
    public void requestWithOneParam()
        throws XmlRpcException, JDOMException, IOException
    {
        assertDocument( "requestWithOneParam",
                        (JDomRenderer) new JDomRenderer().startRequest()
                                                         .requestMethod( "foo" )
                                                         .startParameter( 0 )
                                                         .value( "test", ValueType.STRING )
                                                         .parameter( 0, "test", ValueType.STRING )
                                                         .endParameter()
                                                         .endRequest() );
    }

    @Test
    public void simpleResponse()
        throws JDOMException, IOException, XmlRpcException
    {
        assertDocument( "simpleResponse", (JDomRenderer) new JDomRenderer().startResponse()
                                                                           .fault( 101, "foo" )
                                                                           .endResponse() );
    }

    @Test
    public void requestWithOneSingleElementArrayParam()
        throws XmlRpcException, JDOMException, IOException
    {
        assertDocument( "requestWithOneArrayParam",
                        (JDomRenderer) new JDomRenderer().startRequest()
                                                         .requestMethod( "foo" )
                                                         .startParameter( 0 )
                                                         .startArray()
                                                         .startArrayElement( 0 )
                                                         .value( "test", ValueType.STRING )
                                                         .arrayElement( 0, "test", ValueType.STRING )
                                                         .endArrayElement()
                                                         .endArray()
                                                         .parameter( 0, new ExtList<String>( "test" ), ValueType.ARRAY )
                                                         .endParameter()
                                                         .endRequest() );
    }

    @Test
    public void requestWithOneSingleMemberStructParam()
        throws XmlRpcException, JDOMException, IOException
    {
        assertDocument( "requestWithOneStructParam",
                        (JDomRenderer) new JDomRenderer().startRequest()
                                                         .requestMethod( "foo" )
                                                         .startParameter( 0 )
                                                         .startStruct()
                                                         .startStructMember( "key" )
                                                         .value( "test", ValueType.STRING )
                                                         .structMember( "key", "test", ValueType.STRING )
                                                         .endStructMember()
                                                         .endStruct()
                                                         .parameter( 0, new ExtMap<String, String>( "key", "test" ),
                                                                     ValueType.STRUCT )
                                                         .endParameter()
                                                         .endRequest() );
    }

    @Test
    public void requestWithArrayInStruct()
        throws XmlRpcException, JDOMException, IOException
    {
        assertDocument(
                        "requestWithArrayInStruct",
                        (JDomRenderer) new JDomRenderer().startRequest()
                                                         .requestMethod( "foo" )
                                                         .startParameter( 0 )
                                                         .startStruct()
                                                         .startStructMember( "key" )
                                                         .startArray()
                                                         .startArrayElement( 0 )
                                                         .value( "test", ValueType.STRING )
                                                         .arrayElement( 0, "test", ValueType.STRING )
                                                         .endArrayElement()
                                                         .endArray()
                                                         .structMember( "key", new ExtList<String>( "test" ),
                                                                        ValueType.ARRAY )
                                                         .endStructMember()
                                                         .endStruct()
                                                         .parameter(
                                                                     0,
                                                                     new ExtMap<String, ExtList<String>>(
                                                                                                          "key",
                                                                                                          new ExtList<String>(
                                                                                                                               "test" ) ),
                                                                     ValueType.STRUCT )
                                                         .endParameter()
                                                         .endRequest() );
    }

    private void assertDocument( final String checkDocName, final JDomRenderer renderer )
        throws JDOMException, IOException
    {
        final Document check = readDocument( checkDocName );

        final Document result = renderer.getDocument();

        final XMLOutputter out = new XMLOutputter( Format.getCompactFormat() );
        assertEquals( out.outputString( check ), out.outputString( result ) );
    }

    private Document readDocument( final String name )
        throws JDOMException, IOException
    {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource( DOC_PATH + name + ".xml" );
        return builder.build( resource.openStream() );
    }

}
