package org.commonjava.rwx2.util;

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import org.apache.commons.lang.StringUtils;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx2.core.Renderer;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ruhan on 7/31/17.
 */
public class ProcessorUtils
{
    public static final String GENERATED = "generated";

    public static <E> Set<? extends E> union( Set<? extends E>... sets )
    {
        Set<E> es = new HashSet<E>();
        for ( Set<? extends E> s : sets )
        {
            es.addAll( s );
        }
        return es;
    }

    public static String[] getPackageAndClassName( String classQName )
    {
        String packageName = null;
        int lastDot = classQName.lastIndexOf( '.' );
        if ( lastDot > 0 )
        {
            packageName = classQName.substring( 0, lastDot );
        }
        String simpleClassName = classQName.substring( lastDot + 1 );
        return new String[] { packageName, simpleClassName };
    }

    public static List<Object> getList( Map<Integer, Object> paramsMap )
    {
        List<Object> ret = new ArrayList<>( paramsMap.size() );
        for ( int i = 0; i < paramsMap.size(); i++ )
        {
            ret.add( paramsMap.get( i ) );
        }
        return ret;
    }

    public static String getMethodName( String prefix, Element e )
    {
        String fieldName = e.getSimpleName().toString();

        Character upperCaseChar = Character.toUpperCase( fieldName.charAt( 0 ) );
        StringBuilder sb = new StringBuilder( prefix );
        sb.append( upperCaseChar );
        sb.append( fieldName.substring( 1 ) );
        return sb.toString();
    }

    /**
     * Get package name out of an assortment of package names to name the RWX Registry class. e.g., input
     * org.apache.commons.cli
     * org.apache.commons.codec
     * We then choose the common top level package 'org.apache.commons' and name the registry class accordingly as
     * org.apache.commons.generated.Commons_Registry
     * If we get more than one top packages, e.g., 'org.apache.commons' and 'com.yourcompany', we simply return 'generated._Registry'
     * I am pretty sure there is better way to implement this. Let me know if you can make it with less code.
     * @param packageNames
     * @return
     */
    public static String getRegistryClassName( Set<String> packageNames )
    {
        String commonPkgName = null;

        if ( packageNames.size() == 1 )
        {
            commonPkgName = packageNames.toArray( new String[0] )[0];
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            while ( true )
            {
                Character cur = null;
                boolean stop = false;
                for ( String packageName : packageNames )
                {
                    try
                    {
                        Character c = packageName.charAt( index );
                        if ( cur == null )
                        {
                            cur = c;
                        }
                        else
                        {
                            if ( cur.equals( c ) )
                            {
                                ; // do nothing
                            }
                            else
                            {
                                stop = true;
                                break;
                            }
                        }
                    }
                    catch ( StringIndexOutOfBoundsException e )
                    {
                        stop = true;
                        break;
                    }
                }
                if ( stop || cur == null )
                {
                    break;
                }
                sb.append( cur );
                index++;
            }

            commonPkgName = sb.toString();
            if ( StringUtils.isBlank( commonPkgName ) )
            {
                return "generated._Registry"; // default
            }

            if ( commonPkgName.endsWith( "." ) )
            {
                commonPkgName = commonPkgName.substring( 0, commonPkgName.length() );
            }
            else
            {
                int lastDot = commonPkgName.lastIndexOf( "." );
                commonPkgName = commonPkgName.substring( 0, lastDot );
            }
        }

        String[] split = getPackageAndClassName( commonPkgName );
        String lastName = split[1];
        Character upperCaseChar = Character.toUpperCase( lastName.charAt( 0 ) );
        StringBuilder simpleName = new StringBuilder();
        simpleName.append( upperCaseChar );
        simpleName.append( lastName.substring( 1 ) );

        String ret = commonPkgName + "." + GENERATED + "." + simpleName.toString() + "_Registry";
        return ret;
    }

    public static String getParserClassName( String type )
    {
        String[] split = getPackageAndClassName( type );
        String packageName = split[0];
        String simpleClassName = split[1];
        return packageName + "." + GENERATED + "." + simpleClassName + "_Parser";
    }

    public static String getRendererClassName( String type )
    {
        String[] split = getPackageAndClassName( type );
        String packageName = split[0];
        String simpleClassName = split[1];
        return packageName + "." + GENERATED + "." + simpleClassName + "_Renderer";
    }
/*
    public static Object renderArray( List<Object> objects, Class<?> rendererClass ) throws XmlRpcException
    {
        if ( rendererClass == null )
        {
            return objects;
        }

        List<Object> list = new ArrayList<>();
        try
        {
            for ( Object obj : objects )
            {
                list.add( ( (Renderer) rendererClass.newInstance() ).render( obj ) );
            }

        }
        catch ( Exception e )
        {
            throw new XmlRpcException( "Render array error", e );
        }
        return list;
    }*/
}
