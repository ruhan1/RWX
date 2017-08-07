package org.commonjava.rwx2.core;

import org.commonjava.rwx2.error.XmlRpcException;
import org.commonjava.rwx2.model.Fault;
import org.commonjava.rwx2.model.MethodCall;
import org.commonjava.rwx2.model.MethodResponse;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by ruhan on 7/13/17.
 */
public class XmlRpcParserTest
                extends AbstractTest
{

    @Test
    public void simpleRequestTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "simpleRequest" ) );
        Object object = parser.parse();

        assertTrue( object instanceof MethodCall );

        MethodCall request = (MethodCall) object;

        String methodName = request.getMethodName();
        assertTrue( "foo".equals( methodName ) );
    }

    @Test
    public void requestWithOneParamTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "requestWithOneParam" ) );
        Object object = parser.parse();

        MethodCall request = (MethodCall) object;
        List<Object> params = request.getParams();

        assertTrue( params.size() == 1 );
        assertTrue( "test".equals( params.get( 0 ) ) );

        String methodName = request.getMethodName();
        assertTrue( "foo".equals( methodName ) );
    }

    @Test
    public void requestWithOneStructParamTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "requestWithOneStructParam" ) );
        Object object = parser.parse();

        MethodCall request = (MethodCall) object;
        List<Object> params = request.getParams();

        assertTrue( params.size() == 1 );

        Object param = params.get( 0 );
        assertTrue( param instanceof Map );

        Map<String, Object> struct = (Map) param;
        assertTrue( "test".equals( struct.get( "key" ) ) );

        String methodName = request.getMethodName();
        assertTrue( "foo".equals( methodName ) );
    }

    @Test
    public void requestWithOneArrayParamTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "requestWithOneArrayParam" ) );
        Object object = parser.parse();

        MethodCall request = (MethodCall) object;
        List<Object> params = request.getParams();

        assertTrue( params.size() == 1 );

        Object param = params.get( 0 );
        assertTrue( param instanceof List );

        List<Object> array = (List) param;
        assertTrue( "test".equals( array.get( 0 ) ) );

        String methodName = request.getMethodName();
        assertTrue( "foo".equals( methodName ) );
    }

    @Test
    public void requestWithArrayInStructTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "requestWithArrayInStruct" ) );
        Object object = parser.parse();

        MethodCall request = (MethodCall) object;
        List<Object> params = request.getParams();

        assertTrue( params.size() == 1 );

        Object param = params.get( 0 );
        assertTrue( param instanceof Map );

        Map<String, Object> struct = (Map) param;

        List<Object> array = (List) struct.get( "key" );
        assertTrue( "test".equals( array.get( 0 ) ) );

        String methodName = request.getMethodName();
        assertTrue( "foo".equals( methodName ) );
    }

    @Test
    public void kojiMulticallRequestTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "kojiMulticallRequest" ) );
        Object object = parser.parse();

        String nvr = "org.dashbuilder-dashbuilder-parent-metadata-0.4.0.Final-1";

        MethodCall request = (MethodCall) object;
        List<Object> params = request.getParams();

        assertTrue( params.size() == 1 );

        List<Object> array = (List) params.get( 0 );

        Map<String, Object> value1 = (Map) array.get( 0 );
        assertEquals( "getBuild", value1.get( "methodName" ) );
        assertEquals( nvr, ( (List) value1.get( "params" ) ).get( 0 ) );

        Map<String, Object> value2 = (Map) array.get( 1 );
        assertEquals( "listTags", value2.get( "methodName" ) );
        assertEquals( nvr, ( (List) value2.get( "params" ) ).get( 0 ) );

        String methodName = request.getMethodName();
        assertTrue( "multiCall".equals( methodName ) );
    }

    @Test
    public void simpleResponseTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "simpleResponse" ) );
        Object object = parser.parse();

        assertTrue( object instanceof MethodResponse );

        MethodResponse response = (MethodResponse) object;
        List<Object> params = response.getParams();

        assertTrue( params.size() == 1 );

        Object p = params.get( 0 );
        assertTrue( p instanceof Double );
        assertTrue( ( (Double) p ).doubleValue() == 18.24668429131D );
    }

    @Test
    public void simpleFaultResponseTest() throws IOException, XMLStreamException, XmlRpcException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "simpleFault" ) );
        Object object = parser.parse();

        assertTrue( object instanceof Fault );

        Fault fault = (Fault) object;
        Object value = fault.getValue();

        assertTrue( value instanceof Map );

        Map<String, Object> m = (Map<String, Object>) value;

        Object p = m.get( "faultCode" );
        assertTrue( p instanceof Integer );
        assertTrue( ( (Integer) p ).intValue() == 101 );

        p = m.get( "faultString" );
        assertTrue( p instanceof String );
        assertTrue( "foo".equals( p ) );
    }

    @Test
    public void jiraServerInfoTest() throws IOException, XMLStreamException, XmlRpcException, ParseException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "jiraServerInfo" ) );
        Object object = parser.parse();

        MethodResponse response = (MethodResponse) object;

        Object p = response.getParams().get( 0 );
        assertTrue( p instanceof Map );

        Map<String, Object> struct = (Map<String, Object>) p;

        assertTrue( struct.get( "version" ).equals( "4.1.2" ) );
        assertTrue( struct.get( "edition" ).equals( "Enterprise" ) );
        assertTrue( struct.get( "buildNumber" ).equals( "531" ) );
        assertTrue( struct.get( "buildDate" ).equals( "Mon Jun 07 00:00:00 CDT 2010" ) );
    }

    @Test
    public void kojiGetBuildResponseTest() throws IOException, XMLStreamException, XmlRpcException, ParseException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "kojiGetBuildResponse" ) );
        Object object = parser.parse();

        MethodResponse response = (MethodResponse) object;

        Object p = response.getParams().get( 0 );
        assertTrue( p instanceof Map );

        Map<String, Object> struct = (Map<String, Object>) p;

        assertTrue( struct.get( "package_name" ).equals( "org.dashbuilder-dashbuilder-parent-metadata" ) );
        assertTrue( struct.get( "extra" ) == null );
        assertTrue( struct.get( "creation_time" ).equals( "2016-09-15 21:24:04.843726" ) );
        assertTrue( struct.get( "package_id" ).equals( 48475 ) );
        assertTrue( struct.get( "completion_ts" ).equals( Double.parseDouble( "1473974644.95959" ) ) );
    }

    @Test
    public void kojiMulticallResponseTest() throws IOException, XMLStreamException, XmlRpcException, ParseException
    {
        final XmlRpcParser parser = new XmlRpcParser( getXMLStream( "kojiMulticallResponse" ) );
        Object object = parser.parse();

        MethodResponse response = (MethodResponse) object;

        Object p = response.getParams().get( 0 );
        assertTrue( p instanceof List );

        List<Object> array = (List) p;

        List<Object> array1 = (List) array.get( 0 );
        Map<String, Object> struct = (Map) array1.get( 0 );
        assertEquals( "org.dashbuilder-dashbuilder-parent-metadata", struct.get( "package_name" ) );
        assertEquals( null, struct.get( "extra" ) );
        assertEquals( 48475, struct.get( "package_id" ) );
        assertEquals( "0.4.0.Final", struct.get( "version" ) );

        List<Object> array2 = (List) array.get( 1 );
        List<Object> array2_1 = (List) array2.get( 0 );

        Map<String, Object> tag1 = (Map) array2_1.get( 0 );
        assertEquals( true, tag1.get( "maven_support" ) );
        assertEquals( "jb-bxms-6.3-candidate", tag1.get( "name" ) );

        Map<String, Object> tag2 = (Map) array2_1.get( 1 );
        assertEquals( true, tag2.get( "maven_support" ) );
        assertEquals( "jb-cs-maven-candidate", tag2.get( "name" ) );

        Map<String, Object> tag3 = (Map) array2_1.get( 2 );
        assertEquals( true, tag3.get( "maven_support" ) );
        assertEquals( "jb-fis-2.0-maven-imports", tag3.get( "name" ) );

        Map<String, Object> tag4 = (Map) array2_1.get( 3 );
        assertEquals( true, tag4.get( "maven_support" ) );
        assertEquals( "jb-mm-7.0-maven-candidate", tag4.get( "name" ) );
    }

}
