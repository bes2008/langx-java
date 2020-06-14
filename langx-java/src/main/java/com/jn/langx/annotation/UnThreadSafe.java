package com.jn.langx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * a mark annotation
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
@Target(value = {TYPE, FIELD, PARAMETER})
public @interface UnThreadSafe {
}
