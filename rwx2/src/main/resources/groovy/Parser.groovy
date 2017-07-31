package ${parserPackageName};

import org.commonjava.rwx2.core.Parser;
import org.commonjava.rwx2.model.RpcObject;

import ${qName};

import java.util.List;
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
        Map<String, Object> params = (Map) object;
        <% params.each { %>
            <% if (it.actionClass == null) { %>
        ret.${it.methodName}((${it.type}) params.get("${it.key}"));
            <% } else { %>
        ret.${it.methodName}(new ${it.actionClass}().parse( params.get("${it.key}")) );
            <% } %>
        <% } %>
        <% } else { %>
        <% if (arrayPart == true) { %>
        List<Object> params = (List)object;
        <% } else { %>
        List<Object> params = ((RpcObject) object).getParams();
        <% } %>
        <% params.eachWithIndex { it, idx -> %>
            <% if (it.actionClass == null) { %>
        ret.${it.methodName}((${it.type}) params.get(${idx}));
            <% } else { %>
        ret.${it.methodName}(new ${it.actionClass}().parse( params.get(${idx})) );
            <% } %>
        <% } %>
        <% } %>
        return ret;
    }
}
