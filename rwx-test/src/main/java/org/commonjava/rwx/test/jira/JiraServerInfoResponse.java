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
package org.commonjava.rwx.test.jira;

import org.commonjava.rwx.anno.Converter;
import org.commonjava.rwx.anno.DataIndex;
import org.commonjava.rwx.anno.Response;

/**
 * Created by ruhan on 8/7/17.
 */
@Response
public class JiraServerInfoResponse
{
    public AbstractJiraServerInfo getValue()
    {
        return value;
    }

    public void setValue( AbstractJiraServerInfo value )
    {
        this.value = value;
    }

    @Converter( JiraServerInfoConverter.class )
    @DataIndex( 0 )
    private AbstractJiraServerInfo value;

}
