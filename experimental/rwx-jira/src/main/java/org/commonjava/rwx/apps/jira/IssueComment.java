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

import org.commonjava.rwx.binding.anno.BindVia;
import org.commonjava.rwx.binding.anno.KeyRefs;
import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx.binding.anno.UnbindVia;
import org.commonjava.rwx.binding.convert.FlexibleDateConverter;

import java.util.Date;

@StructPart
public class IssueComment
{

    private final String id;

    private final String body;

    private final String username;

    @BindVia( FlexibleDateConverter.class )
    @UnbindVia( FlexibleDateConverter.class )
    private Date timePerformed;

    public IssueComment( final String id, final String body, final String username )
    {
        this.id = id;
        this.body = body;
        this.username = username;
        timePerformed = new Date();
    }

    @KeyRefs( { "id", "body", "username", "timePerformed" } )
    public IssueComment( final String id, final String body, final String username, final Date timePerformed )
    {
        this.id = id;
        this.body = body;
        this.username = username;
        this.timePerformed = timePerformed;
    }

    public String getId()
    {
        return id;
    }

    public String getBody()
    {
        return body;
    }

    public String getUsername()
    {
        return username;
    }

    public Date getTimePerformed()
    {
        return timePerformed;
    }

    public void setTimePerformed( final Date timePerformed )
    {
        this.timePerformed = timePerformed;
    }

    @Override
    public String toString()
    {
        return "Comment for " + id + " [timePerformed=" + timePerformed + ", username=" + username + "]\n\n" + body;
    }

}
