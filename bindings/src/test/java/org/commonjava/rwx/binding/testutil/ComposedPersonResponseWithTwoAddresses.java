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

package org.commonjava.rwx.binding.testutil;

import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Response;

@Response
public class ComposedPersonResponseWithTwoAddresses
{

    @DataIndex( 0 )
    private String userId;

    @DataIndex( 1 )
    private String firstName;

    @DataIndex( 2 )
    private String lastName;

    @DataIndex( 3 )
    private String email;

    @DataIndex( 4 )
    private SimpleAddress homeAddress;

    @DataIndex( 5 )
    private SimpleAddress businessAddress;

    public SimpleAddress getHomeAddress()
    {
        return homeAddress;
    }

    public void setHomeAddress( final SimpleAddress address )
    {
        homeAddress = address;
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

    public void setEmail( final String email )
    {
        this.email = email;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( final String userId )
    {
        this.userId = userId;
    }

    public SimpleAddress getBusinessAddress()
    {
        return businessAddress;
    }

    public void setBusinessAddress( final SimpleAddress businessAddress )
    {
        this.businessAddress = businessAddress;
    }

}
