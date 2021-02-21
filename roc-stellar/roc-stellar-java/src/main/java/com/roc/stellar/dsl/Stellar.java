package com.roc.stellar.dsl;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Stellar
 * <p>
 *
 * @author jelly.wang
 * @create 2021/02/20
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Stellar {

    String name();

    String description() default "";

    String returns() default "";

    String[] params() default {};
}
