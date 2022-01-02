package com.jn.langx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 用于标记扩展的第三方库的代码。
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = {TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
public @interface Extension {
}
