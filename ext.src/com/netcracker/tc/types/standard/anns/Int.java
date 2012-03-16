package com.netcracker.tc.types.standard.anns;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.tc.types.standard.IntType;

/**
 * Annotation for {@link IntType} parameters.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER,FIELD})
public @interface Int
{
    int min() default Integer.MIN_VALUE;
    
    int max() default Integer.MAX_VALUE;
}
