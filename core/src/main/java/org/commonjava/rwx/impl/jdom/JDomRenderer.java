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

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.join;
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

    private List<Object> callsAndChanges = new ArrayList<>();

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

    public String renderCallsAndChanges()
    {
        return join( callsAndChanges, "\n" );
    }

    @Override
    public JDomRenderer fault( final int code, final String message )
        throws XmlRpcException
    {
        record();
        final Element fault = new Element( FAULT );
        addParentContent( fault );

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
        record();
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "START-PARAM: {}", index );

        verifyParamsContainer();

        final Element param = new Element( PARAM );
        addParentContent( param );

        final Element value = new Element( VALUE );
        param.addContent( value );

        setParent( value );
        return this;
    }

    @Override
    public JDomRenderer value( final Object value, final ValueType type )
        throws CoercionException
    {
        record();
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "RAW-VALUE: {}, TYPE: {}", value, type );

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
        record();
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.debug( "START: {}", methodName );

        final Element mn = new Element( METHOD_NAME );
        mn.setText( methodName );

        addParentContent( mn );
        return this;
    }

    @Override
    public JDomRenderer startArray()
    {
        record();
        final Element arry = new Element( ARRAY );
        addParentContent( arry );

        final Element data = new Element( DATA );
        arry.addContent( data );

        setParent( data );
        return this;
    }

    @Override
    public JDomRenderer startArrayElement( final int index )
    {
        record();
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "START-ARRAY-ELEMENT: {}", index );

        final Element value = new Element( VALUE );
        addParentContent( value );

        setParent( value );
        return this;
    }

    @Override
    public JDomRenderer startRequest()
    {
        record();
        setParent( new Element( REQUEST ) );
        doc = new Document( currentParent );
        return this;
    }

    @Override
    public JDomRenderer startResponse()
    {
        record();
        setParent( new Element( RESPONSE ) );
        doc = new Document( currentParent );
        return this;
    }

    @Override
    public JDomRenderer startStruct()
    {
        record();
        final Element e = new Element( STRUCT );
        addParentContent( e );

        setParent( e );
        return this;
    }

    private void addParentContent( Element e )
    {
        if ( currentParent == null )
        {
            currentParent = e;
            if ( doc == null )
            {
                doc = new Document( currentParent );
            }
        }
        else
        {
            currentParent.addContent( e );
        }
    }

    @Override
    public JDomRenderer endArray()
    {
        record();
        popParent();
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endArrayElement()
    {
        record();
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endParameter()
    {
        record();
        popParent();
        popParent();
        return this;
    }

    private void record()
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        if ( logger.isTraceEnabled() )
        {
            StackTraceElement call = Thread.currentThread().getStackTrace()[2];
            callsAndChanges.add( call );
        }
    }

    @Override
    public JDomRenderer endStruct()
    {
        record();
        popParent();
        return this;
    }

    @Override
    public JDomRenderer endStructMember()
        throws XmlRpcException
    {
        record();
        popParent();
        popParent();
        return this;
    }

    @Override
    public JDomRenderer startStructMember( final String key )
        throws XmlRpcException
    {
        record();
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.trace( "START-STRUCT-MEMBER: {}", key );

        final Element wrapper = new Element( MEMBER );
        addParentContent( wrapper );

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
            addParentContent( params );

            setParent( params );
        }
    }

    protected void popParent()
    {
        Logger logger = LoggerFactory.getLogger( getClass() );

        if ( logger.isTraceEnabled() )
        {
//            logger.trace( "POP/before: parent is: {}", currentParent );
        }


        setParent( currentParent.getParentElement() );
//        logger.trace( "POP/after: parent is: {}", currentParent );
    }

    protected Element setParent( Element e )
    {
        Logger logger = LoggerFactory.getLogger( getClass() );

        if ( logger.isTraceEnabled() )
        {
            callsAndChanges.add( String.format( "%s (%d) >> %s (%d)", currentParent, depthOf(currentParent), e, depthOf(e) ) );
//            logger.trace( "SET/before: parent is: {}", currentParent );
        }

        currentParent = e;
//        logger.trace( "SET/after: parent is: {}", currentParent );
        return e;
    }

    private int depthOf( Element e )
    {
        int count=0;
        while ( e != null )
        {
            e = e.getParentElement();
            count++;
        }

        return count;
    }

}
