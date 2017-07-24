package ${rendererPackageName};

import org.commonjava.rwx2.core.Renderer;
import org.commonjava.rwx2.model.MethodCall;

import ${qName};

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RWX AnnoProcessor.
 */
public class ${simpleClassName}_Renderer
        implements Renderer<${simpleClassName}>
{
    @Override
    public MethodCall render( ${simpleClassName} request )
    {
        MethodCall ret = new MethodCall();
        ret.setMethodName( "${methodName}" );

        List<Object> params = new ArrayList<>();
        ret.setParams( params );

        <% params.each { %>
        params.add( request.${it}() );
        <% } %>

        return ret;
    }
}
