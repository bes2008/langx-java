package com.jn.langx.security;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * JCA Algorithm Description
 * @author fs1194361820@163.com
 *
 */
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
    public Class[] type();

    /**
     * 算法描述
     *
     * @return
     */
    public String desc() default "";

    public String keyPairAlogithm() default "";
}
