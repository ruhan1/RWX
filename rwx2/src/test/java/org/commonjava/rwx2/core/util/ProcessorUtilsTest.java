package org.commonjava.rwx2.core.util;

import static org.commonjava.rwx2.util.ProcessorUtils.getRegistryClassName;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 8/1/17.
 */
public class ProcessorUtilsTest
{
    @Test
    public void getRegistryNameTest()
    {
        String reg = getRegistryClassName( Collections.emptySet() );
        assertEquals( "generated._Registry", reg );

        Set<String> set = new HashSet<>(  );
        set.add( "org.apache.commons.cli" );
        set.add( "org.apache.commons.codec");

        reg = getRegistryClassName( set );
        assertEquals( "org.apache.commons.generated.Commons_Registry", reg );

        set.add( "com.yourcompany");
        reg = getRegistryClassName( set );
        assertEquals( "generated._Registry", reg );
    }
}
