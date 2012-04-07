package com.netcracker.sova.annotated.anns;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Scenario} parameter.
 * Used to specify parameter title and description,
 * Use {@link Default} to set parameter default value.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER, FIELD})
public @interface Param
{
    /**
     * Parameter title for display.
     */
    String value() default "";

    String description() default "";
}
