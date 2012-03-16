package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks {@link Scenario} parameter,
 * as class, containing other {@link Param}.
 * Scenario must have only one parameter, if it
 * uses this type of passing parameters.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER})
public @interface Params
{

}
