/**
 * Copyright (C) 2010 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.anno.DataKey;
import org.commonjava.rwx.anno.StructPart;

import static org.commonjava.rwx.test.subclass.Constants.EXTRA_INFO;
import static org.commonjava.rwx.test.subclass.Constants.NAME;
import static org.commonjava.rwx.test.subclass.Constants.RELEASE;
import static org.commonjava.rwx.test.subclass.Constants.VERSION;

/**
 * Created by ruhan on 12/19/17.
 */
@StructPart
public class BuildDescription
{
    @DataKey( NAME )
    private String name;

    @DataKey( VERSION )
    private String version;

    @DataKey( RELEASE )
    private String release;

    @DataKey( EXTRA_INFO )
    private BuildExtraInfoExtended extraInfo;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getRelease()
    {
        return release;
    }

    public void setRelease( String release )
    {
        this.release = release;
    }

    public BuildExtraInfoExtended getExtraInfo()
    {
        return extraInfo;
    }

    public void setExtraInfo( BuildExtraInfoExtended extraInfo )
    {
        this.extraInfo = extraInfo;
    }
}
