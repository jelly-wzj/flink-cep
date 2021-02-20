package com.roc.stellar.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stellar
 * <p>
 *
 * @author jelly.wang
 * @create 2021/02/20
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Stellar {
    String namespace() default "";

    String name();

    String description() default "";

    String returns() default "";

    String[] params() default {};
}
