package com.netcracker.tc.tests.anns;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.tc.model.Type;

/**
 * {@link Scenario} parameter.
 * Must be public field.
 * Parameter field also may be annotated with 
 * type-specific annotation for more type configuration.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD})
public @interface Parameter
{
    /**
     * Parameter title.
     * Default value is field name,
     * converter from camelCase into "Camel case"
     */
    String value() default "";

    String description() default "";
    
    /**
     * Parameter type.
     * Default value is considered from field type.
     */
    Class<? extends Type> type() default Type.class;
}
