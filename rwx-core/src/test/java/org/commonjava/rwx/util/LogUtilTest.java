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

package org.commonjava.rwx.util;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import java.io.StringWriter;

public class LogUtilTest
{

    @Test
    public void formatOneParam()
    {
        final StringWriter sWriter = new StringWriter();

        final Logger logger = Logger.getLogger( LogUtilTest.class );
        logger.addAppender( new WriterAppender( new SimpleLayout(), sWriter ) );

        LogUtil.info( logger, "This is a %s", "test" );

        assertEquals( "INFO - This is a test\n", sWriter.toString() );
    }

}
