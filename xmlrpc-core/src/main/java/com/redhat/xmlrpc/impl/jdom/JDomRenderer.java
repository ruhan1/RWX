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

package com.redhat.xmlrpc.impl.jdom;

import org.jdom.Document;
import org.jdom.Element;

import com.redhat.xmlrpc.error.CoercionException;
import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.spi.AbstractXmlRpcListener;
import com.redhat.xmlrpc.vocab.ValueType;
import com.redhat.xmlrpc.vocab.XmlRpcConstants;

public class JDomRenderer
    extends AbstractXmlRpcListener
{

    private Document doc;

    private Element currentParent;

    public Document getDocument()
    {
        return doc;
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
