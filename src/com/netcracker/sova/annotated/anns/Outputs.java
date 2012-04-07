package com.netcracker.sova.annotated.anns;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks class as container for multiple {@link Output}s
 * of {@link Scenario},
 * each output of this result can be referenced independently.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface Outputs
{

}
