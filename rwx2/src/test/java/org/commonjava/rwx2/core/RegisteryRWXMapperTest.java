package org.commonjava.rwx2.core;

import org.commonjava.rwx2.core.simple.generated.TestRegistry;
import org.junit.BeforeClass;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ruhan on 7/19/17.
 */
public class RegisteryRWXMapperTest
                extends RWXMapperTest
{
    @BeforeClass
    public static void register()
    {
        Registry.setInstance( new TestRegistry() );
    }

}

