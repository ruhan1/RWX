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
