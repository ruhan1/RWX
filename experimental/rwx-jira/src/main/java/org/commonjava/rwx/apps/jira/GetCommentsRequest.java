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
import org.commonjava.rwx.binding.anno.Request;

@Request( method = "jira1.getComments" )
public class GetCommentsRequest
{

    @DataIndex( 0 )
    private String token;

    @DataIndex( 1 )
    private String id;

    public GetCommentsRequest( final String id )
    {
        token = id;
        this.id = id;
    }

    public GetCommentsRequest( final String token, final String id )
    {
        this.token = token;
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setToken( final String token )
    {
        this.token = token;
    }

    public void setId( final String id )
    {
        this.id = id;
    }

    public String getToken()
    {
        return token;
    }

}
