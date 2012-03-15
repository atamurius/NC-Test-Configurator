package com.netcracker.tc.types.standard.anns;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.tc.tests.anns.Parameter;
import com.netcracker.tc.types.standard.EnumType;
import com.netcracker.tc.types.standard.SetType;

/**
 * {@link Parameter} annotation for {@link EnumType} or {@link SetType}.
 * If field type is enum, enum values are used,
 * otherwise you must set {@link #value}. 
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD})
public @interface Values
{
    /**
     * Values as full method name (package.Class.method).
     * Method must be public static, without parameters and
     * return array or iterable.
     */
    String value() default "";
}
