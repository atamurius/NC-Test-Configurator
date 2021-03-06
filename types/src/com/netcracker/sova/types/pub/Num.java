package com.netcracker.sova.types.pub;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.sova.types.num.IntType;
import com.netcracker.sova.types.num.RealType;


/**
 * Annotation for {@link IntType} or {@link RealType} parameters.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER,FIELD})
public @interface Num
{
    double min() default Double.NaN;
    
    double max() default Double.NaN;
}
