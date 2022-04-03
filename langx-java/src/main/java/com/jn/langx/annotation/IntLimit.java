package com.jn.langx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = {FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})
public @interface IntLimit {
    /**
     * 默认值
     */
    int value() default 0;

    int max() default Integer.MAX_VALUE;

    int min() default Integer.MIN_VALUE;
}
