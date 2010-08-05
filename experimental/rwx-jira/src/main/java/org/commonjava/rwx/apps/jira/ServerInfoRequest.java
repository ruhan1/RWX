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

package org.commonjava.rwx.apps.jira;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.IndexRefs;
import org.commonjava.rwx.binding.anno.Request;

@Request( method = "jira1.getServerInfo" )
public class ServerInfoRequest
{

    public static final String VERSION = "version";

    @DataIndex( 0 )
    private final String infoType;

    @IndexRefs( 0 )
    public ServerInfoRequest( final String infoType )
    {
        this.infoType = infoType;
    }

    @IndexRefs( 0 )
    public ServerInfoRequest()
    {
        infoType = null;
    }

    public String getInfoType()
    {
        return infoType;
    }

}
