package com.netcracker.tc.types.standard.anns;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.tc.tests.anns.Parameter;
import com.netcracker.tc.types.standard.RefType;

/**
 * {@link Parameter} annotation for {@link RefType}.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD})
@Inherited
public @interface Ref
{
    /**
     * Reference type label.
     * Default value is field type class name.
     */
    String value() default "";
    
    /**
     * If reference is required or null can be used.
     */
    boolean required() default true;
}
