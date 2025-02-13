package com.jn.langx.security.crypto.cipher;

/**
 * BytesCipher接口继承自Cipher接口，用于定义操作字节数据的加密和解密规范
 * 它限定了加密和解密方法的输入和输出类型为字节数组，从而确保了在处理数据时的一致性和通用性
 */
public interface BytesCipher extends Cipher<byte[], byte[]> {
    /**
     * 加密给定的字节数组
     * 此方法接收一个字节数组作为输入，并返回加密后的字节数组
     * 它确保了数据在传输或存储过程中的安全性
     *
     * @param text 待加密的原始字节数组
     * @return 加密后的字节数组
     */
    @Override
    byte[] encrypt(byte[] text);

    /**
     * 解密给定的字节数组
     * 此方法接收一个加密过的字节数组作为输入，并返回解密后的字节数组
     * 它允许用户在接收到加密数据后恢复原始信息
     *
     * @param encryptedText 加密后的字节数组
     * @return 解密后的原始字节数组
     */
    @Override
    byte[] decrypt(byte[] encryptedText);
}
