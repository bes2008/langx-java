package com.jn.langx.security;

import java.lang.annotation.*;

/**
 * JCA Algorithm Description
 * @author fs1194361820@163.com
 *
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Algorithm {
    /**
     * 算法名称
     *
     * @return
     */
    public String name();

    @SuppressWarnings("rawtypes")
    /**
     * 算法类型，可取值有：{Signature, MessageDigest, KeyPairGenerator, or ParameterGenerator}
     * @return
     */
    public Class[] apply();

    /**
     * 算法描述
     *
     * @return
     */
    public String desc() default "";
}
