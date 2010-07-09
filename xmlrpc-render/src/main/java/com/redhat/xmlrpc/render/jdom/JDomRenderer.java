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

package com.redhat.xmlrpc.render.jdom;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcFault;
import com.redhat.xmlrpc.raw.model.XmlRpcRequest;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;
import com.redhat.xmlrpc.render.XmlRpcRenderer;
import com.redhat.xmlrpc.render.jdom.helper.RequestHelper;
import com.redhat.xmlrpc.render.jdom.helper.ResponseHelper;

public class JDomRenderer
    implements XmlRpcRenderer
{

    private static final RequestHelper REQUEST_HELPER = new RequestHelper();

    private static final ResponseHelper RESPONSE_HELPER = new ResponseHelper();

    @Override
    public String render( final XmlRpcRequest request )
        throws XmlRpcException
    {
        final Document doc = new Document();
        final Element root = REQUEST_HELPER.render( request );

        doc.setRootElement( root );

        return new XMLOutputter( Format.getCompactFormat() ).outputString( doc );
    }

    @Override
    public String render( final XmlRpcResponse response )
        throws XmlRpcException
    {
        final Document doc = new Document();
        final Element root = RESPONSE_HELPER.render( response );

        doc.setRootElement( root );

        return new XMLOutputter( Format.getCompactFormat() ).outputString( doc );
    }

    @Override
    public String render( final XmlRpcFault fault )
        throws XmlRpcException
    {
        final Document doc = new Document();

        final XmlRpcResponse response = new XmlRpcResponse( fault );
        final Element root = RESPONSE_HELPER.render( response );

        doc.setRootElement( root );

        return new XMLOutputter( Format.getCompactFormat() ).outputString( doc );
    }

}
