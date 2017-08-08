package ${rendererPackageName};

import org.commonjava.rwx.core.Renderer;
import org.commonjava.rwx.model.MethodCall;
import org.commonjava.rwx.model.MethodResponse;

import ${qName};

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by RWX AnnoProcessor.
 */
public class ${simpleClassName}_Renderer implements Renderer<${simpleClassName}>
{
    @Override
    public Object render( ${simpleClassName} object )
    {
        <% if (request == true) { %>
        MethodCall ret = new MethodCall();
        ret.setMethodName( "${methodName}" );
        <% } else if (response == true) { %>
        MethodResponse ret = new MethodResponse();
        <% } else if (structPart == true) { %>
        Map<String, Object> ret = new HashMap<>();
            <% params.each { %>
                <% if (it.converter != null) { %>
        ret.put( "${it.key}", new ${it.converter}().render( object.${it.methodName}() ) );
                <% } else if (it.actionClass == null) { %>
        ret.put( "${it.key}", object.${it.methodName}() );
                <% } else { %>
                    <% if (it.contains) { %>
        List<Object> ${it.localListVariableName} = new ArrayList<>(  );
        for ( ${it.elementClass} obj : object.${it.methodName}() )
        {
            ${it.localListVariableName}.add( new ${it.actionClass}().render( obj ) );
        }
        ret.put( "${it.key}", ${it.localListVariableName} );
                    <% } else { %>
        ret.put( "${it.key}", new ${it.actionClass}().render( object.${it.methodName}() ) );
                    <% } %>
                <% } %>
            <% } %>
        <% } else if (arrayPart == true) { %>
        List<Object> ret = new ArrayList<>();
            <% params.each { %>
                <% if (it.converter != null) { %>
        ret.add( new ${it.converter}().render( object.${it.methodName}() ) );
                <% } else if (it.actionClass == null) { %>
        ret.add( object.${it.methodName}() );
                <% } else { %>
                    <% if (it.contains) { %>
        List<Object> ${it.localListVariableName} = new ArrayList<>(  );
        for ( ${it.elementClass} obj : object.${it.methodName}() )
        {
            ${it.localListVariableName}.add( new ${it.actionClass}().render( obj ) );
        }
        ret.add( ${it.localListVariableName} );
                    <% } else { %>
        ret.add( new ${it.actionClass}().render( object.${it.methodName}() ) );
                    <% } %>
                <% } %>
            <% } %>
        <% } %>
        <% if (request == true || response == true) { %>
        List<Object> params = new ArrayList<>();
        ret.setParams( params );
            <% params.each { %>
                <% if (it.converter != null) { %>
        params.add( new ${it.converter}().render( object.${it.methodName}() ) );
                <% } else if (it.actionClass == null) { %>
        params.add( object.${it.methodName}() );
                <% } else { %>
                    <% if (it.contains) { %>
        List<Object> ${it.localListVariableName} = new ArrayList<>(  );
        for ( ${it.elementClass} obj : object.${it.methodName}() )
        {
            ${it.localListVariableName}.add( new ${it.actionClass}().render( obj ) );
        }
        params.add( ${it.localListVariableName} );
                    <% } else { %>
        params.add( new ${it.actionClass}().render( object.${it.methodName}() ) );
                    <% } %>
                <% } %>
            <% } %>
        <% } %>
        return ret;
    }
}
