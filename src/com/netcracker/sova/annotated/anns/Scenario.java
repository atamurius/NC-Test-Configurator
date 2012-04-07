package com.netcracker.sova.annotated.anns;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Scenario method annotation,
 * contains parameters, (marked as {@link Param} if needed),
 * 
 * Scenario must be non-static method of
 * {@link Scenarios} annotated class.
 * 
 * Scenario can return one result (in this case method should be
 * annotated with {@link Output}, or object of class,
 * marked as {@link Outputs}
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
@Inherited
public @interface Scenario
{
    /**
     * Scenario title.
     * Default value is method name.
     */
    String value() default "";
}



