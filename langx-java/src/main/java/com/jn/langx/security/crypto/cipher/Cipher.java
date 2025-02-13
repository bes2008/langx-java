package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.SecurityException;

/**
 * Cipher接口定义了加密和解密操作的规范。
 * 它是一个泛型接口，允许在编译时指定原始数据类型和加密后的数据类型。
 *
 * @param <ORIGIN>   原始数据类型
 * @param <ENCRYPTED 加密后的数据类型
 */
public interface Cipher<ORIGIN, ENCRYPTED> {

    /**
     * 加密操作。
     * 将给定的原始文本加密为指定的返回类型。
     *
     * @param text 原始文本
     * @return 加密后的文本
     */
    ENCRYPTED encrypt(ORIGIN text);

    /**
     * 解密操作。
     * 将给定的加密文本解密为原始类型。
     * 此方法在遇到不符合格式的加密文本时会抛出IllegalArgumentException，
     * 在其他异常情况下会抛出SecurityException。
     *
     * @param encryptedText 加密后的文本
     * @return 原始文本
     * @throws IllegalArgumentException 当参数 encryptedText 的格式不符合时，会抛出该异常
     * @throws SecurityException 其他情况下抛出该异常
     */
    ORIGIN decrypt(ENCRYPTED encryptedText);

}
