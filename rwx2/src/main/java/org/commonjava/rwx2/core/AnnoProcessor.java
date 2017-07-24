package org.commonjava.rwx2.core;

import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;
import org.apache.commons.io.IOUtils;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.anno.Response;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ruhan on 7/24/17.
 */
@SupportedAnnotationTypes( "org.commonjava.rwx.binding.anno.*" )
public class AnnoProcessor
                extends AbstractProcessor
{
    private void debug( String message )
    {
        System.out.println( getClass().getSimpleName() + " >> " + message );
    }

    public static final String TEMPLATE_PKG = "groovy";

    public static final String RENDERER_TEMPLATE = "Renderer.groovy";
    public static final String PARSER_TEMPLATE = "Parser.groovy";

    final GStringTemplateEngine engine = new GStringTemplateEngine();

    @Override
    public boolean process( Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment )
    {
        debug( "Processing for " + annotations );

        Template rendererTemplate = getTemplate( RENDERER_TEMPLATE );
        Template parserTemplate = getTemplate( PARSER_TEMPLATE );

        try
        {
            Set<? extends Element> requestClasses = roundEnvironment.getElementsAnnotatedWith( Request.class );
            for ( Element elem : requestClasses )
            {
                writeRendererFile( (TypeElement) elem, roundEnvironment, rendererTemplate );
            }

            Set<? extends Element> responseClasses = roundEnvironment.getElementsAnnotatedWith( Response.class );
            for ( Element elem : responseClasses )
            {
                writeParserFile( (TypeElement) elem, roundEnvironment, parserTemplate );
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "RWX AnnoProcessor error", e );
        }

        return true;
    }

    private void writeParserFile( TypeElement respElement, RoundEnvironment roundEnvironment, Template parserTemplate )
                    throws IOException
    {
    }

    private void writeRendererFile( TypeElement reqElement, RoundEnvironment roundEnvironment,
                                    Template rendererTemplate ) throws IOException
    {
        String qName = reqElement.getQualifiedName().toString();

        String packageName = null;
        int lastDot = qName.lastIndexOf( '.' );
        if ( lastDot > 0 )
        {
            packageName = qName.substring( 0, lastDot );
        }
        String simpleClassName = qName.substring( lastDot + 1 );
        String rendererSimpleClassName = simpleClassName + "_Renderer";

        String rendererPackageName = ( packageName == null ) ? "generated" : packageName + ".generated";
        String rendererClassName = rendererPackageName + "." + rendererSimpleClassName;

        debug( "Renderer: " + rendererClassName );

        Request anno = reqElement.getAnnotation( Request.class );
        String methodName = anno.method();

        Map<Integer, Object> paramsMap = new HashMap<>();
        for ( Element e : reqElement.getEnclosedElements() )
        {
            DataIndex dataIndex = e.getAnnotation( DataIndex.class );
            if ( dataIndex != null )
            {
                int index = dataIndex.value();
                paramsMap.put( index, getMethodName( e ) );
            }
        }
        List<Object> params = getList(paramsMap);

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put( "rendererSimpleClassName", methodName );
        templateParams.put( "rendererPackageName", methodName );
        templateParams.put( "qName", qName );
        templateParams.put( "simpleClassName", simpleClassName );
        templateParams.put( "methodName", methodName );
        templateParams.put( "params", params );

        generateOutput( rendererTemplate, templateParams, rendererClassName );
    }

    private List<Object> getList( Map<Integer, Object> paramsMap )
    {
        List<Object> ret = new ArrayList<>(paramsMap.size());
        for(int i = 0; i < paramsMap.size(); i++)
        {
            ret.add( paramsMap.get( i ) );
        }
        return ret;
    }

    private String getMethodName( Element e )
    {
        String fieldName = e.getSimpleName().toString();

        Character upperCaseChar = Character.toUpperCase( fieldName.charAt( 0 ) );
        StringBuilder sb = new StringBuilder( "get" );
        sb.append( upperCaseChar );
        sb.append( fieldName.substring( 1 ) );
        return sb.toString();
    }

    private Template getTemplate( String templateName )
    {
        Template template;
        try
        {
            final FileObject resource = processingEnv.getFiler()
                                                     .getResource( StandardLocation.CLASS_PATH, TEMPLATE_PKG,
                                                                   templateName );
            template = engine.createTemplate( resource.toUri().toURL() );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Cannot load template: " + TEMPLATE_PKG + "/" + templateName, e );
        }
        return template;
    }

    private void generateOutput( Template template, Map<String, Object> templateParams, String rendererClassName )
    {
        Filer filer = processingEnv.getFiler();
        Writable output = template.make( templateParams );
        Writer sourceWriter = null;
        try
        {
            FileObject file = filer.createSourceFile( rendererClassName );
            sourceWriter = file.openWriter();
            output.writeTo( sourceWriter );
        }
        catch ( final IOException e )
        {
            processingEnv.getMessager()
                         .printMessage( Diagnostic.Kind.ERROR,
                                        "While generating sources for class: '" + rendererClassName + "', error: "
                                                        + e.getMessage() );
        }
        finally
        {
            IOUtils.closeQuietly( sourceWriter );
        }
    }

}
