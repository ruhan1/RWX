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
package org.commonjava.rwx.impl.jdom;

import org.apache.commons.lang.StringUtils;
import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.AbstractXmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.commonjava.rwx.vocab.XmlRpcConstants.ARRAY;
import static org.commonjava.rwx.vocab.XmlRpcConstants.DATA;
import static org.commonjava.rwx.vocab.XmlRpcConstants.FAULT;
import static org.commonjava.rwx.vocab.XmlRpcConstants.FAULT_CODE;
import static org.commonjava.rwx.vocab.XmlRpcConstants.FAULT_STRING;
import static org.commonjava.rwx.vocab.XmlRpcConstants.MEMBER;
import static org.commonjava.rwx.vocab.XmlRpcConstants.METHOD_NAME;
import static org.commonjava.rwx.vocab.XmlRpcConstants.NAME;
import static org.commonjava.rwx.vocab.XmlRpcConstants.PARAM;
import static org.commonjava.rwx.vocab.XmlRpcConstants.PARAMS;
import static org.commonjava.rwx.vocab.XmlRpcConstants.REQUEST;
import static org.commonjava.rwx.vocab.XmlRpcConstants.RESPONSE;
import static org.commonjava.rwx.vocab.XmlRpcConstants.STRUCT;
import static org.commonjava.rwx.vocab.XmlRpcConstants.VALUE;

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
        final Element fault = new Element( FAULT );
        currentParent.addContent( fault );

        final Element value = new Element( VALUE );
        fault.addContent( value );

        final Element last = currentParent;
        setParent( value );

        startStruct();
        startStructMember( FAULT_CODE );
        value( code, ValueType.INT );
        structMember( FAULT_CODE, code, ValueType.INT );
        endStructMember();
        startStructMember( FAULT_STRING );
        value( message, ValueType.STRING );
        structMember( FAULT_STRING, message, ValueType.STRING );
        endStructMember();
        endStruct();

        setParent( last );
        return this;
    }

    @Override
    public JDomRenderer startParameter( final int index )
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "START-PARAM: {}", index );

        verifyParamsContainer();

        final Element param = new Element( PARAM );
        currentParent.addContent( param );

        final Element value = new Element( VALUE );
        param.addContent( value );

        setParent( value );
        return this;
    }

    @Override
    public JDomRenderer value( final Object value, final ValueType type )
        throws CoercionException
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "RAW-VALUE: {}, TYPE: {}", value, type.name() );

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
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.debug( "START: {}", methodName );

        final Element mn = new Element( METHOD_NAME );
        mn.setText( methodName );

        currentParent.addContent( mn );
        return this;
    }

    @Override
    public JDomRenderer startArray()
    {
        final Element arry = new Element( ARRAY );
        currentParent.addContent( arry );

        final Element data = new Element( DATA );
        arry.addContent( data );

        setParent( data );
        return this;
    }

    @Override
    public JDomRenderer startArrayElement( final int index )
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "START-ARRAY-ELEMENT: {}", index );

        final Element value = new Element( VALUE );
        currentParent.addContent( value );

        setParent( value );
        return this;
    }

    @Override
    public JDomRenderer startRequest()
    {
        setParent( new Element( REQUEST ) );
        doc = new Document( currentParent );
        return this;
    }

    @Override
    public JDomRenderer startResponse()
    {
        setParent( new Element( RESPONSE ) );
        doc = new Document( currentParent );
        return this;
    }

    @Override
    public JDomRenderer startStruct()
    {
        final Element e = new Element( STRUCT );
        currentParent.addContent( e );

        setParent( e );
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
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "START-STRUCT-MEMBER: {}", key );

        final Element wrapper = new Element( MEMBER );
        currentParent.addContent( wrapper );

        final Element name = new Element( NAME );
        name.setText( key );

        wrapper.addContent( name );

        final Element value = new Element( VALUE );
        wrapper.addContent( value );

        setParent( value );
        return this;
    }

    protected void generateValue( final Element parent, final Object value, final ValueType vt )
        throws CoercionException
    {
        final Element val = new Element( vt.getPrimaryTag() );
        parent.addContent( val );

        val.setText( vt.coercion().toString( value ) );

        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "VALUE: {}", val.getText() );
    }

    protected void verifyParamsContainer()
    {
        if ( !PARAMS.equals( currentParent.getName() ) )
        {
            final Element params = new Element( PARAMS );
            currentParent.addContent( params );

            setParent( params );
        }
    }

    protected void popParent()
    {
        Logger logger = LoggerFactory.getLogger( getClass() );

        if ( logger.isTraceEnabled() )
        {
            logger.trace( "POP/before: parent is: {}\n\nCalled from: {}\n", currentParent,
                               StringUtils.join( Thread.currentThread().getStackTrace(), "\n  " ) );
        }


        setParent( currentParent.getParentElement() );
        logger.trace( "POP/after: parent is: {}", currentParent );
    }

    protected Element setParent( Element e )
    {
        Logger logger = LoggerFactory.getLogger( getClass() );

        if ( logger.isTraceEnabled() )
        {
            logger.trace( "SET/before: parent is: {}\n\nCalled from: {}\n", currentParent,
                               StringUtils.join( Thread.currentThread().getStackTrace(), "\n  " ) );
        }

        currentParent = e;
        logger.trace( "SET/after: parent is: {}", currentParent );
        return e;
    }

}
