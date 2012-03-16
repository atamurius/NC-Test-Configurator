package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Scenario} output field.
 * Must be public field.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD})
public @interface Output
{
    /**
     * Output title, default is field name.
     * For {@link Scenario#output} this value must be set.
     */
    String value() default "";
    
    /**
     * Output type label.
     * Default value is field type class name.
     */
    String type() default "";
}
