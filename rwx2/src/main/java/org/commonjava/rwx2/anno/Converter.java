package org.commonjava.rwx2.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ruhan on 8/7/17.
 */
@Target( { ElementType.FIELD, ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface Converter
{
    Class<?> value();
}
