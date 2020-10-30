package com.jn.langx.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @since 2.10.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface OnMissClasses {

}
