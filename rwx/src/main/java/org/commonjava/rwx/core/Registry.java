package org.commonjava.rwx.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ruhan on 7/19/17.
 */
public class Registry
{

    protected Map<Class, Parser> parserMap = new ConcurrentHashMap<>();
    protected Map<Class, Renderer> rendererMap = new ConcurrentHashMap<>();

    public Parser getParser(Class cls)
    {
        return parserMap.get( cls );
    }

    protected void setParser(Class cls, Parser parser)
    {
        parserMap.put( cls, parser );
    }

    public Renderer getRenderer(Class cls)
    {
        return rendererMap.get( cls );
    }

    protected void setRenderer(Class cls, Renderer renderer)
    {
        rendererMap.put( cls, renderer );
    }


    // singleton instance

    private static Registry instance = new Registry(); // default

    public static synchronized Registry getInstance() {
        return instance;
    }

    public static synchronized void setInstance( Registry registry ) {
        instance = registry;
    }
}
