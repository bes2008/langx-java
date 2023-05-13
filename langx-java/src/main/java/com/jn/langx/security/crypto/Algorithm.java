package com.jn.langx.security.crypto;

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
     * @return the algorithm name
     */
    String name();


    /**
     * 算法类型，可取值有：{Signature, MessageDigest, KeyPairGenerator, or ParameterGenerator}
     * @return the used scene
     */
    @SuppressWarnings("rawtypes") Class[] apply();

    /**
     * 算法描述
     *
     * @return the description
     */
    String desc() default "";
}
