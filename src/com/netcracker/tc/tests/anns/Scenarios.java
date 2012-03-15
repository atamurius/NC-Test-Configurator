package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Scenarios,
 * merks class, that contains scenarios as
 * public static inner classes,
 * marked as {@link Scenario}
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface Scenarios
{
    /**
     * Scenarios group title.
     * Default value — class short name.
     */
    String value() default "";
}
