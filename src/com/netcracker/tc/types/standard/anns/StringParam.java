package com.netcracker.tc.types.standard.anns;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.tc.tests.anns.Parameter;
import com.netcracker.tc.types.standard.StringType;

/**
 * {@link Parameter} attribute for {@link StringType}.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD})
public @interface StringParam
{
    boolean required() default false;
    
    int max() default 0;
    
    String pattern() default "";
}
