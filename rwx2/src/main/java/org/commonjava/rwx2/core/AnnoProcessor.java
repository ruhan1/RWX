package org.commonjava.rwx2.core;

import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;
import org.apache.commons.io.IOUtils;
import org.commonjava.rwx.binding.anno.ArrayPart;
import org.commonjava.rwx.binding.anno.Contains;
import org.commonjava.rwx.binding.anno.DataIndex;
import org.commonjava.rwx.binding.anno.DataKey;
import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.anno.Response;
import org.commonjava.rwx.binding.anno.StructPart;
import org.commonjava.rwx2.util.ProcessorUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.commonjava.rwx2.util.ProcessorUtils.GENERATED;
import static org.commonjava.rwx2.util.ProcessorUtils.getElementClassByType;
import static org.commonjava.rwx2.util.ProcessorUtils.getList;
import static org.commonjava.rwx2.util.ProcessorUtils.getMethodName;
import static org.commonjava.rwx2.util.ProcessorUtils.getPackageAndClassName;
import static org.commonjava.rwx2.util.ProcessorUtils.getRegistryClassName;
import static org.commonjava.rwx2.util.ProcessorUtils.union;

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

    public static final String REGISTRY_TEMPLATE = "Registry.groovy";

    final GStringTemplateEngine engine = new GStringTemplateEngine();

    @Override
    public boolean process( Set<? extends TypeElement> annotations, RoundEnvironment roundEnv )
    {
        debug( "Processing for " + annotations );

        Template rendererTemplate = getTemplate( RENDERER_TEMPLATE );
        Template parserTemplate = getTemplate( PARSER_TEMPLATE );
        Template registryTemplate = getTemplate( REGISTRY_TEMPLATE );

        try
        {
            Set<? extends Element> requestClasses = roundEnv.getElementsAnnotatedWith( Request.class );
            Set<? extends Element> responseClasses = roundEnv.getElementsAnnotatedWith( Response.class );
            Set<? extends Element> structClasses = roundEnv.getElementsAnnotatedWith( StructPart.class );
            Set<? extends Element> arrayClasses = roundEnv.getElementsAnnotatedWith( ArrayPart.class );

            Set<? extends Element> classes = union( requestClasses, responseClasses, structClasses, arrayClasses );
            for ( Element elem : classes )
            {
                writeRendererFile( (TypeElement) elem, roundEnv, rendererTemplate );
                writeParserFile( (TypeElement) elem, roundEnv, parserTemplate );
            }
            if ( !classes.isEmpty() )
            {
                writeRegistryFile( classes, roundEnv, registryTemplate );
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "RWX AnnoProcessor error", e );
        }

        return true;
    }

    private void writeRegistryFile( Set<? extends Element> classes, RoundEnvironment roundEnvironment,
                                    Template registryTemplate )
    {
        List<String> imports = new ArrayList<>();
        List<String> simpleClassNames = new ArrayList<>();

        Set<String> packageNames = new HashSet<>();
        for ( Element elem : classes )
        {
            String qName = ( (TypeElement) elem ).getQualifiedName().toString();
            imports.add( qName );
            String[] split = getPackageAndClassName( qName );
            String packageName = split[0];
            String simpleClassName = split[1];
            imports.add( packageName + "." + GENERATED + "." + simpleClassName + "_Renderer" );
            imports.add( packageName + "." + GENERATED + "." + simpleClassName + "_Parser" );
            simpleClassNames.add( simpleClassName );
            packageNames.add( packageName );
        }

        String registryClassName = getRegistryClassName( packageNames );
        String[] split = getPackageAndClassName( registryClassName );
        String registryPackageName = split[0];
        String registrySimpleClassName = split[1];

        debug( "Registry: " + registryClassName );

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put( "packageName", registryPackageName );
        templateParams.put( "registrySimpleClassName", registrySimpleClassName );
        templateParams.put( "imports", imports );
        templateParams.put( "classes", simpleClassNames );

        generateOutput( registryTemplate, templateParams, registryClassName );
    }

    private void writeParserFile( TypeElement typeElement, RoundEnvironment roundEnvironment, Template template )
                    throws IOException
    {
        String qName = typeElement.getQualifiedName().toString();
        String[] split = getPackageAndClassName( qName );

        String packageName = split[0];
        String simpleClassName = split[1];

        String parserSimpleClassName = simpleClassName + "_Parser";
        String parserPackageName = ( packageName == null ) ? GENERATED : packageName + "." + GENERATED;
        String parserClassName = parserPackageName + "." + parserSimpleClassName;

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put( "parserPackageName", parserPackageName );
        templateParams.put( "qName", qName );
        templateParams.put( "simpleClassName", simpleClassName );
        templateParams.put( "structPart", false );
        templateParams.put( "arrayPart", false );

        StructPart structPart = typeElement.getAnnotation( StructPart.class );
        if ( structPart != null )
        {
            handleStructPart( templateParams, typeElement, "set", ProcessorUtils::getParserClassName );
        }
        else
        {
            ArrayPart arrayPart = typeElement.getAnnotation( ArrayPart.class );
            if ( arrayPart != null )
            {
                templateParams.put( "arrayPart", true );
            }
            handleArrayPart( templateParams, typeElement, "set", ProcessorUtils::getParserClassName );
        }

        generateOutput( template, templateParams, parserClassName );
    }

    private void writeRendererFile( TypeElement typeElement, RoundEnvironment roundEnvironment, Template template )
                    throws IOException
    {
        String qName = typeElement.getQualifiedName().toString();
        String[] split = getPackageAndClassName( qName );

        String packageName = split[0];
        String simpleClassName = split[1];

        String rendererSimpleClassName = simpleClassName + "_Renderer";
        String rendererPackageName = ( packageName == null ) ? GENERATED : packageName + "." + GENERATED;
        String rendererClassName = rendererPackageName + "." + rendererSimpleClassName;

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put( "rendererPackageName", rendererPackageName );
        templateParams.put( "qName", qName );
        templateParams.put( "simpleClassName", simpleClassName );
        templateParams.put( "request", false );
        templateParams.put( "response", false );
        templateParams.put( "structPart", false );
        templateParams.put( "arrayPart", false );

        Request request = typeElement.getAnnotation( Request.class );
        Response response = typeElement.getAnnotation( Response.class );

        if ( request != null || response != null )
        {
            if ( request != null )
            {
                templateParams.put( "request", true );
                templateParams.put( "methodName", request.method() );
            }
            else
            {
                templateParams.put( "response", true );
            }
            handleArrayPart( templateParams, typeElement, "get", ProcessorUtils::getRendererClassName );
        }

        StructPart structPart = typeElement.getAnnotation( StructPart.class );
        if ( structPart != null )
        {
            handleStructPart( templateParams, typeElement, "get", ProcessorUtils::getRendererClassName );
        }

        ArrayPart arrayPart = typeElement.getAnnotation( ArrayPart.class );
        if ( arrayPart != null )
        {
            templateParams.put( "arrayPart", true );
            handleArrayPart( templateParams, typeElement, "get", ProcessorUtils::getRendererClassName );
        }

        generateOutput( template, templateParams, rendererClassName );
    }

    private void handleArrayPart( Map<String, Object> templateParams, TypeElement typeElement, String method,
                                  Function<String, String> function )
    {
        Map<Integer, Object> paramsMap = new HashMap<>();
        for ( Element e : typeElement.getEnclosedElements() )
        {
            DataIndex dataIndex = e.getAnnotation( DataIndex.class );
            if ( dataIndex != null )
            {
                Item item = getItemObj( e, method, function );
                paramsMap.put( dataIndex.value(), item );
            }
        }
        templateParams.put( "params", getList( paramsMap ) );
    }

    private void handleStructPart( Map<String, Object> templateParams, TypeElement typeElement, String method,
                                   Function<String, String> function )
    {
        templateParams.put( "structPart", true );
        List<Object> params = new ArrayList<>();
        for ( Element e : typeElement.getEnclosedElements() )
        {
            DataKey dataKey = e.getAnnotation( DataKey.class );
            if ( dataKey != null )
            {
                Item item = getItemObj( e, method, function );
                item.setKey( dataKey.value() );
                params.add( item );
            }
        }
        templateParams.put( "params", params );
    }

    private Item getItemObj( Element e, String method, Function<String, String> function )
    {
        Item item = new Item();
        String type = e.asType().toString();
        String actionClass = getActionClass( type, function );

        String elementClass = getElementClass( e );
        if ( elementClass != null )
        {
            debug( e.toString() + ": contains=true, elementClass=" + elementClass );
            item.setElementClass( elementClass );
            item.setContains( true );
            item.setLocalListVariableName( e.getSimpleName().toString() );
            actionClass = getActionClass( elementClass, function );
        }

        item.setMethodName( getMethodName( method, e ) );
        item.setType( type );
        item.setKey( null );
        item.setActionClass( actionClass );

        return item;
    }

    private String getElementClass( Element e )
    {
        String elementClass = null;
        Contains contains = e.getAnnotation( Contains.class );
        if ( contains != null ) // array of @Request, @Response, @arrayPart, or @structPart annotated objects
        {
            // https://stackoverflow.com/questions/7687829/java-6-annotation-processing-getting-a-class-from-an-annotation
            try
            {
                Class<?> c = contains.value();
            }
            catch ( MirroredTypeException mte )
            {
                elementClass = mte.getTypeMirror().toString();
            }
        }
        else
        {
            elementClass = getElementClassByType( e.asType().toString() );

        }
        return elementClass;
    }

    /**
     * Get parser or renderer class name of a field by the real type.
     */
    private String getActionClass( String type, Function<String, String> function )
    {
        String actionClass = null;
        TypeElement typeElement = processingEnv.getElementUtils().getTypeElement( type );
        if ( typeElement != null )
        {
            StructPart sPart = typeElement.getAnnotation( StructPart.class );
            ArrayPart aPart = typeElement.getAnnotation( ArrayPart.class );
            if ( sPart != null || aPart != null )
            {
                actionClass = function.apply( type );
            }
        }
        return actionClass;
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

    private void generateOutput( Template template, Map<String, Object> templateParams, String className )
    {
        Filer filer = processingEnv.getFiler();
        Writable output = template.make( templateParams );
        Writer sourceWriter = null;
        try
        {
            FileObject file = filer.createSourceFile( className );
            sourceWriter = file.openWriter();
            output.writeTo( sourceWriter );
        }
        catch ( final IOException e )
        {
            processingEnv.getMessager()
                         .printMessage( Diagnostic.Kind.ERROR,
                                        "While generating sources for class: '" + className + "', error: "
                                                        + e.getMessage() );
        }
        finally
        {
            IOUtils.closeQuietly( sourceWriter );
        }
    }

    static class Item
    {
        private boolean contains;

        private String methodName;

        private String type;

        private String key;

        private String actionClass;

        private String elementClass;

        private String localListVariableName;

        public Item()
        {
        }

        public String getMethodName()
        {
            return methodName;
        }

        public String getType()
        {
            return type;
        }

        public String getKey()
        {
            return key;
        }

        public String getActionClass()
        {
            return actionClass;
        }

        public void setMethodName( String methodName )
        {
            this.methodName = methodName;
        }

        public void setType( String type )
        {
            this.type = type;
        }

        public void setKey( String key )
        {
            this.key = key;
        }

        public void setActionClass( String actionClass )
        {
            this.actionClass = actionClass;
        }

        public boolean getContains()
        {
            return contains;
        }

        public void setContains( boolean b )
        {
            contains = b;
        }

        public String getElementClass()
        {
            return elementClass;
        }

        public void setElementClass( String elementClass )
        {
            this.elementClass = elementClass;
        }

        public String getLocalListVariableName()
        {
            return localListVariableName;
        }

        public void setLocalListVariableName( String localListVariableName )
        {
            this.localListVariableName = localListVariableName;
        }
    }
}
