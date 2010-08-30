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

package org.commonjava.rwx.binding.internal.xbr.helper;

import static org.commonjava.rwx.binding.anno.AnnotationUtils.hasAnnotation;

import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

public class StructWrapperBinder
    extends AbstractBinder
    implements Binder
{

    private Object value;

    public StructWrapperBinder( final Binder parent, final Class<?> valueType, final XBRBindingContext context )
    {
        super( parent, valueType, context );
    }

    @Override
    protected Binder endStructInternal()
        throws XmlRpcException
    {
        @SuppressWarnings( "unchecked" )
        final ValueType vt = hasAnnotation( getType(), ArrayPart.class ) ? ValueType.ARRAY : ValueType.STRUCT;
        setValue( value, vt );
        return this;
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( value == null )
        {
            this.value = value;
        }

        return this;
    }

}
