package com.jn.langx.security.crypto.cipher;

public interface StringCipher extends Cipher<String, String> {
    @Override
    String encrypt(String text);

    @Override
    String decrypt(String encryptedText);
}
