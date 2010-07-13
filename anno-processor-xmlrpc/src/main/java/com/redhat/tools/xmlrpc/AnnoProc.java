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

package com.redhat.tools.xmlrpc;

import com.redhat.xmlrpc.binding.anno.Request;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import java.util.Set;

@SupportedAnnotationTypes( { "com.redhat.xmlrpc.binding.anno.Request", "com.redhat.xmlrpc.binding.anno.Response",
    "com.redhat.xmlrpc.binding.anno.DataIndex", "com.redhat.xmlrpc.binding.anno.MessagePart",
    "com.redhat.xmlrpc.binding.anno.DataKey", "com.redhat.xmlrpc.binding.anno.ValueConverter",
    "com.redhat.xmlrpc.binding.anno.IndexRefs", "com.redhat.xmlrpc.binding.anno.KeyRefs" } )
@SupportedSourceVersion( SourceVersion.RELEASE_6 )
public class AnnoProc
    extends AbstractProcessor
{

    private static final ArrayPartProcessor ARRAY_PROC = new ArrayPartProcessor();

    @Override
    public boolean process( final Set<? extends TypeElement> typeElements, final RoundEnvironment roundEnvironment )
    {
        for ( final Element el : roundEnvironment.getElementsAnnotatedWith( Request.class ) )
        {
            final TypeElement tel = (TypeElement) el;
            final Request req = tel.getAnnotation( Request.class );
            final String method = req.method();

            ARRAY_PROC.process( tel );

            //            final List<? extends Element> elements = tel.getEnclosedElements();
            //            for ( final Element element : elements )
            //            {
            //                final ElementKind kind = element.getKind();
            //                ElementKind.FIELD
            //            }
        }

        return true;
    }
}
