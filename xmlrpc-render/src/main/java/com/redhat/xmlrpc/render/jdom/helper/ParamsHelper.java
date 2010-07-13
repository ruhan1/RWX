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

import java.util.List;

public class ParamsHelper
    implements RendererHelper<List<XmlRpcValue<?>>>
{

    private static final ValueHelper VALUE_HELPER = new ValueHelper();

    @Override
    public Element render( final List<XmlRpcValue<?>> params )
        throws XmlRpcException
    {
        final Element result = new Element( XmlRpcConstants.PARAMS );

        for ( final XmlRpcValue<?> param : params )
        {
            final Element p = new Element( XmlRpcConstants.PARAM );
            result.addContent( p );

            p.addContent( VALUE_HELPER.render( param ) );
        }

        return result;
    }

}
