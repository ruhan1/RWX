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

package com.redhat.xmlrpc.binding.anno;

import java.lang.annotation.Annotation;

public final class AnnotationUtils
{

    private AnnotationUtils()
    {
    }

    public static String getRequestMethod( final Object obj )
    {
        final Class<?> type = ( ( obj instanceof Class<?> ) ? (Class<?>) obj : obj.getClass() );
        final Request req = type.getAnnotation( Request.class );
        if ( req != null )
        {
            return req.method();
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public static boolean isRequest( final Object obj )
    {
        return hasAnnotation( obj, Request.class );
    }

    @SuppressWarnings( "unchecked" )
    public static boolean isResponse( final Object obj )
    {
        return hasAnnotation( obj, Response.class );
    }

    @SuppressWarnings( "unchecked" )
    public static boolean isMessage( final Object obj )
    {
        return hasAnnotation( obj, Response.class, Request.class );
    }

    @SuppressWarnings( "unchecked" )
    public static boolean isMessagePart( final Object obj )
    {
        return hasAnnotation( obj, Response.class, Request.class, ArrayPart.class, StructPart.class );
    }

    public static boolean hasAnnotation( final Object obj, final Class<? extends Annotation>... annotationTypes )
    {
        final Class<?> type = ( ( obj instanceof Class<?> ) ? (Class<?>) obj : obj.getClass() );
        for ( final Class<? extends Annotation> annotationType : annotationTypes )
        {
            if ( type.getAnnotation( annotationType ) != null )
            {
                return true;
            }
        }

        return false;
    }

}
