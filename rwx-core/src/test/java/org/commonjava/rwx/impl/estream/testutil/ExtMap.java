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

package org.commonjava.rwx.impl.estream.testutil;

import java.util.HashMap;

public class ExtMap<K, V>
    extends HashMap<K, V>
{

    private static final long serialVersionUID = 1L;

    public ExtMap( final K key, final V val )
    {
        put( key, val );
    }

    public ExtMap()
    {
    }

    public ExtMap<K, V> with( final K key, final V value )
    {
        put( key, value );
        return this;
    }

}
