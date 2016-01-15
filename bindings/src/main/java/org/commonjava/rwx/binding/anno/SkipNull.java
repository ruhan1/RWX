package org.commonjava.rwx.binding.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jdcasey on 1/14/16.
 */
@Target( {ElementType.FIELD, ElementType.METHOD, ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface SkipNull
{
}
