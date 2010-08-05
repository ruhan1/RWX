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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;
import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.internal.xbr.XBRCompositionBindery;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.http.SyncXmlRpcClient;
import org.commonjava.rwx.http.httpclient4.HC4SyncClient;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class JiraTest
{

    private static String user;

    private static String pass;

    private static String url;

    private static XBRCompositionBindery bindery;

    @BeforeClass
    public static void setupLogging()
    {
        final Configurator log4jConfigurator = new Configurator()
        {
            @SuppressWarnings( "unchecked" )
            public void doConfigure( final URL notUsed, final LoggerRepository repo )
            {
                repo.getRootLogger().addAppender( new ConsoleAppender( new SimpleLayout() ) );

                final Enumeration<Logger> loggers = repo.getCurrentLoggers();
                while ( loggers.hasMoreElements() )
                {
                    final Logger logger = loggers.nextElement();
                    logger.setLevel( Level.DEBUG );
                }
            }
        };

        log4jConfigurator.doConfigure( null, LogManager.getLoggerRepository() );
    }

    @BeforeClass
    public static void collectInfo()
        throws IOException, BindException
    {
        //        final BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
        //        System.out.print( "\nJIRA URL: " );
        //        url = reader.readLine();
        //
        //        System.out.print( "\nUsername: " );
        //        user = reader.readLine();
        //
        //        System.out.print( "\nPassword: " );
        //        pass = reader.readLine();
        //
        //        System.out.println();

        url = "http://jira.codehaus.org/rpc/xmlrpc";
        bindery = XBRCompositionBindery.binderyFor( JIRAMessageConstants.MESSAGE_TYPES );
    }

    @Test
    @Ignore
    public void login()
        throws XmlRpcException
    {
        final SyncXmlRpcClient client = new HC4SyncClient( url, bindery );

        final LoginResponse response = client.call( new LoginRequest( user, pass ), LoginResponse.class );

        System.out.println( response );
    }

    @Test
    public void getServerInfo()
        throws XmlRpcException
    {
        final SyncXmlRpcClient client = new HC4SyncClient( url, bindery );

        final ServerInfoResponse info = client.call( new ServerInfoRequest(), ServerInfoResponse.class );

        System.out.println( info );
    }

    @Test
    public void getCommentsForIssue()
        throws XmlRpcException
    {
        final SyncXmlRpcClient client = new HC4SyncClient( url, bindery );

        final GetCommentsResponse response =
            client.call( new GetCommentsRequest( "MNG-676" ), GetCommentsResponse.class );

        System.out.println( response );
    }

}
