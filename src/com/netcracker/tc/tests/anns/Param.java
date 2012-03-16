package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Scenario} parameter.
 * Used to specify parameter title and description,
 * for scenario method parameters name should be specified,
 * for scenario {@link Params} object fields should be present. 
 * For type-specific attributes use type-specific annotations.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER, FIELD})
public @interface Param
{
    /**
     * Name of parameter,
     * must be used for method param annotation,
     * is not used for {@link Params} object fields.
     */
    String value() default "";

    /**
     * Parameter title.
     * Default value is name,
     * converted from camelCase into "Camel case"
     */
    String title() default "";

    String description() default "";
    
    String VALUE_NONE = "<<< USE DEFAULT TYPE VALUE >>>";
    
    String defValue() default VALUE_NONE;
}
