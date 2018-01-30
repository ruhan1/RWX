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

import static org.commonjava.rwx.test.subclass.Constants.IMPORT_INITIATOR;

/**
 * Created by ruhan on 12/19/17.
 */
@StructPart
public class BuildExtraInfoExtended extends BuildExtraInfo
{
    @DataKey( IMPORT_INITIATOR )
    private String importInitiator;

    public String getImportInitiator()
    {
        return importInitiator;
    }

    public void setImportInitiator( String importInitiator )
    {
        this.importInitiator = importInitiator;
    }
}
