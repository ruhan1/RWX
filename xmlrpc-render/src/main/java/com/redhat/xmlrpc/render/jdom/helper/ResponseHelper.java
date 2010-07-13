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

package com.redhat.xmlrpc.render.jdom.helper;

import org.jdom.Element;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.raw.XmlRpcConstants;
import com.redhat.xmlrpc.raw.model.XmlRpcValue;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;

import java.util.List;

public class ResponseHelper
    implements RendererHelper<XmlRpcResponse>
{

    private static final ParamsHelper PARAMS_RENDERER = new ParamsHelper();

    private static final FaultHelper FAULT_RENDERER = new FaultHelper();

    @Override
    public Element render( final XmlRpcResponse value )
        throws XmlRpcException
    {
        final Element result = new Element( XmlRpcConstants.RESPONSE );

        if ( value.getFault() != null )
        {
            result.addContent( FAULT_RENDERER.render( value.getFault() ) );
        }
        else
        {
            final List<XmlRpcValue<?>> params = value.getParameters();
            if ( params != null )
            {
                result.addContent( PARAMS_RENDERER.render( params ) );
            }
        }

        return result;
    }

}
