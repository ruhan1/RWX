/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.rwx.binding.anno;

import org.commonjava.rwx.binding.SimpleNoParamsRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public final class AnnotationUtils
{

    private AnnotationUtils()
    {
    }

    public static Class<?> getContainsType( final Field field )
    {
        final Contains contains = field == null ? null : field.getAnnotation( Contains.class );
        return contains == null ? Object.class : contains.value();
    }

    public static Class<?> getBindViaType( final Field field )
    {
        final Converter bindVia = field == null ? null : field.getAnnotation( Converter.class );
        return bindVia == null ? Object.class : bindVia.value();
    }

    public static String getRequestMethod( final Object obj )
    {
        if ( obj instanceof SimpleNoParamsRequest )
        {
            return ( (SimpleNoParamsRequest) obj ).getMethodName();
        }
        else
        {
            final Class<?> type = ( ( obj instanceof Class<?> ) ? (Class<?>) obj : obj.getClass() );
            final Request req = type.getAnnotation( Request.class );
            if ( req != null )
            {
                return req.method();
            }
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

    public static <T extends Annotation> T getAnnotation( final Object obj, final Class<T> annoType )
    {
        final Class<?> type = obj instanceof Class<?> ? (Class<?>) obj : obj.getClass();
        return type.getAnnotation( annoType );
    }

}
