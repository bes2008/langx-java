package com.jn.langx.security.crypto.salt;

import com.jn.langx.util.function.Supplier;

/**
 * BytesSaltGenerator 接口继承了 Supplier 接口，用于生成指定长度的字节盐。
 * 这个接口主要用来在安全敏感操作中产生随机盐，比如密码哈希、加密密钥生成等场景。
 * 通过实现此接口，开发者可以自定义盐的生成逻辑，以适应不同的安全需求。
 */
public interface BytesSaltGenerator extends Supplier<Integer, byte[]> {
    /**
     * 生成指定长度的字节盐。
     *
     * @param bytesLength 盐的长度，以字节为单位。
     * @return 生成的字节盐。
     */
    @Override
    byte[] get(Integer bytesLength);
}
