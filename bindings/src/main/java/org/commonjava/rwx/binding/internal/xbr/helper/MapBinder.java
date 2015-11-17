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

import static org.commonjava.rwx.binding.anno.AnnotationUtils.getContainsType;

import org.apache.xbean.recipe.MapRecipe;
import org.commonjava.rwx.binding.anno.Converter;
import org.commonjava.rwx.binding.internal.xbr.XBRBinderInstantiator;
import org.commonjava.rwx.binding.internal.xbr.XBRBindingContext;
import org.commonjava.rwx.binding.spi.Binder;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.spi.XmlRpcListener;
import org.commonjava.rwx.vocab.ValueType;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MapBinder
    extends AbstractBinder
    implements Binder
{

    private final MapRecipe recipe;

    private String currentMember;

    private final Converter bindVia;

    private final Set<String> seenKeys = new HashSet<String>();

    public MapBinder( final Binder parent, final Class<?> mapType, final Field field, final XBRBindingContext context )
    {
        super( parent, getContainsType( field ), context );
        recipe = new MapRecipe( mapType );
        bindVia = field == null ? null : field.getAnnotation( Converter.class );
    }

    @Override
    protected Binder endStructInternal()
        throws XmlRpcException
    {
        setValue( recipe.create(), ValueType.STRUCT );
        return this;
    }

    @Override
    protected Binder startStructMemberInternal( final String key )
        throws XmlRpcException
    {
        final Binder binder;
        if ( bindVia != null )
        {
            binder = XBRBinderInstantiator.newValueBinder( bindVia, this, getType(), getBindingContext() );
        }
        else
        {
            binder = getBindingContext().newBinder( this, getType() );
        }

        if ( binder != null )
        {
            currentMember = key;
            return binder;
        }

        return this;
    }

    @Override
    protected Binder valueInternal( final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( currentMember != null && !seenKeys.contains( currentMember ) )
        {
            recipe.put( currentMember, value );
            seenKeys.add( currentMember );
        }

        return this;
    }

    @Override
    public XmlRpcListener structMember( final String key, final Object value, final ValueType type )
        throws XmlRpcException
    {
        if ( !seenKeys.contains( key ) )
        {
            recipe.put( key, value );
            seenKeys.add( key );
        }

        return this;
    }

    @Override
    protected Binder endStructMemberInternal()
        throws XmlRpcException
    {
        currentMember = null;
        return this;
    }

}
