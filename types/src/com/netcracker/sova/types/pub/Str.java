package com.netcracker.sova.types.pub;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.types.string.StringType;

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
    int max() default 0;
    
    String pattern() default "";
}
