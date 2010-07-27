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

package org.commonjava.rwx.apps.jira;

import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.IndexRefs;
import org.commonjava.rwx.binding.anno.Response;

import java.util.Iterator;
import java.util.List;

@Response
public class GetCommentsResponse
    implements Iterable<IssueComment>
{

    @DataIndex( 0 )
    @Contains( IssueComment.class )
    private final List<IssueComment> comments;

    @IndexRefs( 0 )
    public GetCommentsResponse( final List<IssueComment> comments )
    {
        this.comments = comments;
    }

    public List<IssueComment> getComments()
    {
        return comments;
    }

    @Override
    public Iterator<IssueComment> iterator()
    {
        return comments.iterator();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        sb.append( "Comments:" );
        for ( final IssueComment comment : this )
        {
            sb.append( "\n\n" ).append( comment );
        }

        sb.append( "\n" );

        return sb.toString();
    }

}
