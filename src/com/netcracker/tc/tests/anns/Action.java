package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Scenario} entry point method annotation.
 * Must be exactly one per scenario.
 * Must not have any parameters, return value is discarded.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
@Inherited
public @interface Action
{

}
