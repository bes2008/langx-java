package com.jn.langx.security.crypto.cipher;

/**
 * StringCipher接口继承自Cipher接口，专门用于字符串的加密和解密操作
 * 它定义了如何对字符串进行加密和解密的方法，使得实现该接口的类能够处理字符串数据
 */
public interface StringCipher extends Cipher<String, String> {
    /**
     * 加密给定的文本
     * 此方法接收一个字符串作为输入，并返回加密后的字符串形式的结果
     * 它抽象了加密过程，隐藏了加密算法的细节
     *
     * @param text 需要加密的原始文本
     * @return 加密后的字符串
     */
    @Override
    String encrypt(String text);

    /**
     * 解密给定的加密文本
     * 此方法接收一个加密字符串作为输入，并返回解密后的字符串形式的结果
     * 它抽象了解密过程，隐藏了解密算法的细节
     *
     * @param encryptedText 需要解密的加密文本
     * @return 解密后的字符串
     */
    @Override
    String decrypt(String encryptedText);
}
