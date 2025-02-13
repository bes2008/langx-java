package com.jn.langx.security.crypto.salt;

import com.jn.langx.util.function.Supplier;

/**
 * StringSaltGenerator 接口用于生成指定长度的字符串盐值
 * 它继承了 Supplier 接口，并重写了 get 方法，使得能够根据给定的字符长度生成字符串盐值
 */
public interface StringSaltGenerator extends Supplier<Integer, String> {
    /**
     * 生成指定长度的字符串盐值
     *
     * @param charsLength 盐值字符串的长度
     * @return 生成的字符串盐值
     */
    @Override
    String get(Integer charsLength);
}
