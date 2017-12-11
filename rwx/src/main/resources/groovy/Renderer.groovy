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
        MethodCall methodCall = new MethodCall();
        methodCall.setMethodName( "${methodName}" );
        <% } else if (response == true) { %>
        MethodResponse methodResponse = new MethodResponse();
        <% } %>

        <% if (structPart == true) { %>
        Map<String, Object> map = new HashMap<>();
            <% params.each { %>
                <% if (it.converter != null) { %>
        map.put( "${it.key}", new ${it.converter}().render( object.${it.methodName}() ) );
                <% } else if (it.actionClass == null) { %>
        map.put( "${it.key}", object.${it.methodName}() );
                <% } else { %>
        if ( object.${it.methodName}() != null )
        {
                    <% if (it.contains) { %>
            List<Object> ${it.localListVariableName} = new ArrayList<>(  );
            for ( ${it.elementClass} obj : object.${it.methodName}() )
            {
                ${it.localListVariableName}.add( new ${it.actionClass}().render( obj ) );
            }
            map.put( "${it.key}", ${it.localListVariableName} );
                    <% } else { %>
            map.put( "${it.key}", new ${it.actionClass}().render( object.${it.methodName}() ) );
                    <% } %>
        }
                <% } %>
            <% } %>
        <% } else { %>
        List<Object> list = new ArrayList<>();
            <% params.each { %>
                <% if (it.converter != null) { %>
        list.add( new ${it.converter}().render( object.${it.methodName}() ) );
                <% } else if (it.actionClass == null) { %>
        list.add( object.${it.methodName}() );
                <% } else { %>
        if ( object.${it.methodName}() != null )
        {
                    <% if (it.contains) { %>
            List<Object> ${it.localListVariableName} = new ArrayList<>(  );
            for ( ${it.elementClass} obj : object.${it.methodName}() )
            {
                ${it.localListVariableName}.add( new ${it.actionClass}().render( obj ) );
            }
            list.add( ${it.localListVariableName} );
                    <% } else { %>
            list.add( new ${it.actionClass}().render( object.${it.methodName}() ) );
                    <% } %>
        }
        else
        {
            list.add( null );
        }
                <% } %>
            <% } %>
        <% } %>        

        <% if (request == true) { %>
        methodCall.setParams( list );
        return methodCall;
        <% } else if (response == true) { %>
        methodResponse.setParams( list );
        return methodResponse;
        <% } else if (arrayPart == true) { %>
        return list;
        <% } else { %>
        map.values().removeAll( java.util.Collections.singleton( null ) ); 
        return map;
        <% } %>
    }
}
