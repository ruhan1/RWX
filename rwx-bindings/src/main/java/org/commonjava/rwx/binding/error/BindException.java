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

package org.commonjava.rwx.binding.error;

import org.commonjava.rwx.error.XmlRpcException;

public class BindException
    extends XmlRpcException
{

    private static final long serialVersionUID = 1L;

    public BindException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public BindException( final String message )
    {
        super( message );
    }

}
