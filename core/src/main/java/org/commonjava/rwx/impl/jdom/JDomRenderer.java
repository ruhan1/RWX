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

import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.commonjava.rwx.vocab.XmlRpcConstants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class JDomRenderer
    extends AbstractXmlRpcListener
{

    private Document doc;

    private Element currentParent;

    private final XMLOutputter outputter;

    public JDomRenderer()
    {
        outputter = new XMLOutputter( Format.getCompactFormat() );
    }

    public JDomRenderer( final XMLOutputter outputter )
    {
        this.outputter = outputter;
    }

    public Document getDocument()
    {
        return doc;
    }

    public String documentToString()
    {
        return doc == null ? null : outputter.outputString( doc );
    }

    @Override
    public JDomRenderer fault( final int code, final String message )
        throws XmlRpcException
    {
        final Element fault = new Element( XmlRpcConstants.FAULT );
        currentParent.addContent( fault );

        final Element value = new Element( XmlRpcConstants.VALUE );
        fault.addContent( value );

        final Element last = currentParent;
        currentParent = value;

        startStruct();
        startStructMember( XmlRpcConstants.FAULT_CODE );
        value( code, ValueType.INT );
        structMember( XmlRpcConstants.FAULT_CODE, code, ValueType.INT );
        endStructMember();
        startStructMember( XmlRpcConstants.FAULT_STRING );
        value( message, ValueType.STRING );
        structMember( XmlRpcConstants.FAULT_STRING, message, ValueType.STRING );
        endStructMember();
        endStruct();

        currentParent = last;
        return this;
    }

    @Override
    public JDomRenderer startParameter( final int index )
    {
        verifyParamsContainer();

        final Element param = new Element( XmlRpcConstants.PARAM );
        currentParent.addContent( param );

        final Element value = new Element( XmlRpcConstants.VALUE );
        param.addContent( value );

        currentParent = value;
        return this;
    }

    @Override
    public JDomRenderer value( final Object value, final ValueType type )
        throws CoercionException
    {
        if ( ValueType.STRUCT == type || ValueType.ARRAY == type )
        {
            return this;
        }

        generateValue( currentParent, value, type );

        return this;
    }

    @Override
    public JDomRenderer requestMethod( final String methodName )
    {
        final Element mn = new Element( XmlRpcConstants.METHOD_NAME );
        mn.setText( methodName );

        currentParent.addContent( mn );
        return this;
    }

    @Override
    public JDomRenderer startArray()
    {
        final Element arry = new Element( XmlRpcConstants.ARRAY );
        currentParent.addContent( arry );

        final Element data = new Element( XmlRpcConstants.DATA );
        arry.addContent( data );

        currentParent = data;
        return this;
    }

    @Override
    public JDomRenderer startArrayElement( final int index )
    {
        final Element value = new Element( XmlRpcConstants.VALUE );
        currentParent.addContent( value );

        currentParent = value;
        return this;
    }

    @Override
    public JDomRenderer startRequest()
    {
        currentParent = new Element( XmlRpcConstants.REQUEST );
        doc = new Document( currentParent );
        return this;
    }

    @Override
    public JDomRenderer startResponse()
    {
        currentParent = new Element( XmlRpcConstants.RESPONSE );
        doc = new Document( currentParent );
        return this;
    }

    @Override
    public JDomRenderer startStruct()
    {
        final Element e = new Element( XmlRpcConstants.STRUCT );
        currentParent.addContent( e );

        currentParent = e;
        return this;
    }

    @Override
    public JDomRenderer endArray()
    {
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endArrayElement()
    {
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endParameter()
    {
        popParent();
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endStruct()
    {
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endStructMember()
        throws XmlRpcException
    {
        popParent();
        popParent();
        return this;
    }

    @Override
    public JDomRenderer startStructMember( final String key )
        throws XmlRpcException
    {
        final Element wrapper = new Element( XmlRpcConstants.MEMBER );
        currentParent.addContent( wrapper );

        final Element name = new Element( XmlRpcConstants.NAME );
        name.setText( key );

        wrapper.addContent( name );

        final Element value = new Element( XmlRpcConstants.VALUE );
        wrapper.addContent( value );

        currentParent = value;
        return this;
    }

    protected void generateValue( final Element parent, final Object value, final ValueType vt )
        throws CoercionException
    {
        final Element val = new Element( vt.getPrimaryTag() );
        parent.addContent( val );

        val.setText( vt.coercion().toString( value ) );
    }

    protected void verifyParamsContainer()
    {
        if ( !XmlRpcConstants.PARAMS.equals( currentParent.getName() ) )
        {
            final Element params = new Element( XmlRpcConstants.PARAMS );
            currentParent.addContent( params );

            currentParent = params;
        }
    }

    protected void popParent()
    {
        currentParent = currentParent.getParentElement();
    }

}
