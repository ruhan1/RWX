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

package org.commonjava.rwx.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public final class LogUtil
{

    private LogUtil()
    {
    }

    public static void trace( final Logger logger, final String template, final Object... params )
    {
        if ( logger != null && logger.isTraceEnabled() )
        {
            logger.trace( format( template, params ) );
        }
    }

    public static void debug( final Logger logger, final String template, final Object... params )
    {
        if ( logger != null && logger.isDebugEnabled() )
        {
            logger.debug( format( template, params ) );
        }
    }

    public static void info( final Logger logger, final String template, final Object... params )
    {
        if ( logger != null && logger.isInfoEnabled() )
        {
            logger.info( format( template, params ) );
        }
    }

    public static void warn( final Logger logger, final String template, final Object... params )
    {
        if ( logger != null && logger.isEnabledFor( Level.WARN ) )
        {
            logger.warn( format( template, params ) );
        }
    }

    public static void error( final Logger logger, final String template, final Object... params )
    {
        if ( logger != null && logger.isEnabledFor( Level.ERROR ) )
        {
            logger.error( format( template, params ) );
        }
    }

    public static void fatal( final Logger logger, final String template, final Object... params )
    {
        if ( logger != null && logger.isEnabledFor( Level.FATAL ) )
        {
            logger.fatal( format( template, params ) );
        }
    }

    private static Object format( final String template, final Object[] params )
    {
        if ( params != null )
        {
            try
            {
                return String.format( template, params );
            }
            catch ( final Exception e )
            {
                return template;
            }
        }

        return template;
    }

}
