package com.netcracker.tc.types.standard.anns;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.tc.tests.anns.Param;
import com.netcracker.tc.types.standard.StringType;

/**
 * {@link Param} attribute for {@link StringType}.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD,PARAMETER})
public @interface Str
{
    boolean required() default false;
    
    int max() default 0;
    
    String pattern() default "";
}
