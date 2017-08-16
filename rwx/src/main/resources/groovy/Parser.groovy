package ${parserPackageName};

import org.commonjava.rwx.core.Parser;
import org.commonjava.rwx.model.RpcObject;

import ${qName};

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by RWX AnnoProcessor.
 */
public class ${simpleClassName}_Parser implements Parser<${simpleClassName}>
{
    @Override
    public ${simpleClassName} parse( Object object )
    {
        ${simpleClassName} ret = new ${simpleClassName}();

        <% if (structPart == true) { %>
        Map<String, Object> map = (Map) object;
        <% params.each { %>
            <% if (it.converter != null) { %>
        ret.${it.methodName}( new ${it.converter}().parse( map.get("${it.key}") ) );
            <% } else if (it.actionClass == null) { %>
        ret.${it.methodName}((${it.type}) map.get("${it.key}"));
            <% } else { %>
        if ( map.get("${it.key}") != null )
        {
                <% if (it.contains) { %>
            List<${it.elementClass}> ${it.localListVariableName} = new ArrayList<>();
            for ( Object obj : ( List<Object> ) map.get("${it.key}") )
            {
                ${it.localListVariableName}.add( new ${it.actionClass}().parse( obj ) );
            }
            ret.${it.methodName}( ${it.localListVariableName} );
                <% } else { %>
            ret.${it.methodName}( new ${it.actionClass}().parse( map.get("${it.key}") ) );
                <% } %>
        }
            <% } %>
        <% } %>
        <% } else { %>
        <% if (arrayPart == true) { %>
        List<Object> list = (List)object;
        <% } else { %>
        List<Object> list = ((RpcObject) object).getParams();
        <% } %>
        <% params.eachWithIndex { it, idx -> %>
            <% if (it.converter != null) { %>
        ret.${it.methodName}( new ${it.converter}().parse( list.get(${idx}) ) );
            <% } else if (it.actionClass == null) { %>
        ret.${it.methodName}((${it.type}) list.get(${idx}));
            <% } else { %>
        if ( list.get(${idx}) != null )
        {
                <% if (it.contains) { %>
            List<${it.elementClass}> ${it.localListVariableName} = new ArrayList<>();
            for ( Object obj : ( List<Object> ) list.get(${idx}) )
            {
                ${it.localListVariableName}.add( new ${it.actionClass}().parse( obj ) );
            }
            ret.${it.methodName}( ${it.localListVariableName} );
                <% } else { %>
            ret.${it.methodName}( new ${it.actionClass}().parse( list.get(${idx}) ) );
                <% } %>
        }
            <% } %>
        <% } %>
        <% } %>
        return ret;
    }
}
