package com.netcracker.sova.types.pub;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.types.ref.RefType;

/**
 * {@link Param} annotation for {@link RefType}.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER,FIELD})
@Inherited
public @interface Ref
{
    /**
     * Reference type label.
     * Default value is field type class name.
     */
    String value() default "";
}
