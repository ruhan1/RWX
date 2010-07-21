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

package com.redhat.xmlrpc.binding.testutil;

import com.redhat.xmlrpc.binding.anno.DataIndex;
import com.redhat.xmlrpc.binding.anno.IndexRefs;
import com.redhat.xmlrpc.binding.anno.Response;

@Response
public class ComposedPersonResponseWithFinalFields
{

    @DataIndex( 0 )
    private final String userId;

    @DataIndex( 1 )
    private String firstName;

    @DataIndex( 2 )
    private String lastName;

    @DataIndex( 3 )
    private final String email;

    @DataIndex( 4 )
    private SimpleFinalFieldAddress address;

    @IndexRefs( { 0, 3 } )
    public ComposedPersonResponseWithFinalFields( final String userId, final String email )
    {
        this.userId = userId;
        this.email = email;
    }

    public SimpleFinalFieldAddress getAddress()
    {
        return address;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( final String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( final String lastName )
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setAddress( final SimpleFinalFieldAddress address )
    {
        this.address = address;
    }

}
