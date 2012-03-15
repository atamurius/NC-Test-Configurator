package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Scenario,
 * contains public fields, marked as {@link Parameter},
 * public fields, marked as {@link Output}
 * and one method, marked as {@link Action}.
 * Scenario can be public static inner class of
 * {@link Scenarios}, or must have `group` attribute.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Inherited
public @interface Scenario
{
    /**
     * Scenario title.
     * Default value is short class name.
     */
    String value() default "";
    
    /**
     * Scenarios group title.
     * Default value is {@link Scenarios#value} of enclosing type.
     */
    String group() default "";
}



