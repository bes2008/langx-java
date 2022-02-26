package com.jn.langx.annotation;

import com.jn.langx.Ordered;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {

    /**
     * 默认是最低优先级,值越小优先级越高
     */
    int value() default Ordered.LOWEST_PRECEDENCE;
}