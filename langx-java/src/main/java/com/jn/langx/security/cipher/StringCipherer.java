package com.jn.langx.security.cipher;

public interface StringCipherer extends Cipherer<String, String> {
    @Override
    String encrypt(String text);

    @Override
    String decrypt(String encryptedText);
}
