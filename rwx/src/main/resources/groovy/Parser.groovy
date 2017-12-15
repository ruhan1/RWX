package ${parserPackageName};

import org.commonjava.rwx.core.Parser;
import org.commonjava.rwx.model.RpcObject;
import static org.commonjava.rwx.util.ParseUtils.nullifyNil;
import static org.commonjava.rwx.util.ParseUtils.isNil;

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
        Object val;

        <% if (structPart == true) { %>
        Map<String, Object> map = (Map) object;
        <% params.each { %>
        val = map.get( "${it.key}" );
        if ( val != null )
        {   
            val = nullifyNil( val );
            <% if (it.converter != null) { %>
            ret.${it.methodName}( new ${it.converter}().parse( val ) );
            <% } else if (it.actionClass == null) { %>
                <% if (it.isPrimitive) { %>
            if ( val != null ) { ret.${it.methodName}( (${it.type}) val ); }
                <% } else { %>
            ret.${it.methodName}( (${it.type}) val );
                <% } %>
            <% } else { %>
                <% if (it.contains) { %>
            List<${it.elementClass}> ${it.localListVariableName} = new ArrayList<>();
            for ( Object obj : ( List<Object> ) val )
            {
                ${it.localListVariableName}.add( new ${it.actionClass}().parse( obj ) );
            }
            ret.${it.methodName}( ${it.localListVariableName} );
                <% } else { %>
            ret.${it.methodName}( new ${it.actionClass}().parse( val ) );
                <% } %>
            <% } %>
        }
        <% } %>
        <% } else { %>
        <% if (arrayPart == true) { %>
        List<Object> list = (List)object;
        <% } else { %>
        List<Object> list = ((RpcObject) object).getParams();
        <% } %>
        <% params.eachWithIndex { it, idx -> %>
        val = list.get( ${idx} );
        if ( val != null && !isNil( val ) )
        {
            <% if (it.converter != null) { %>
            ret.${it.methodName}( new ${it.converter}().parse( val ) );
            <% } else if (it.actionClass == null) { %>
            ret.${it.methodName}( (${it.type}) val );
            <% } else { %>
                <% if (it.contains) { %>
            List<${it.elementClass}> ${it.localListVariableName} = new ArrayList<>();
            for ( Object obj : ( List<Object> ) val )
            {
                ${it.localListVariableName}.add( new ${it.actionClass}().parse( obj ) );
            }
            ret.${it.methodName}( ${it.localListVariableName} );
                <% } else { %>
            ret.${it.methodName}( new ${it.actionClass}().parse( val ) );
                <% } %>
            <% } %>
        }
        <% } %>
        <% } %>
        return ret;
    }
}
