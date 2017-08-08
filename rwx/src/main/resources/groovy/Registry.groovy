package ${packageName};

import org.commonjava.rwx.core.Registry;

<% imports.each { %>
import ${it};<% } %>

/**
 * Created by RWX AnnoProcessor.
 */
public class ${registrySimpleClassName}
        extends Registry
{

    public ${registrySimpleClassName}()
    {
        <% classes.each { %>
        setRenderer( ${it}.class, new ${it}_Renderer() );
        setParser( ${it}.class, new ${it}_Parser() );
        <% } %>
    }

}
