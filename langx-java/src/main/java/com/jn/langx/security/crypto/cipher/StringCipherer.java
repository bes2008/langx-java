package com.jn.langx.security.crypto.cipher;

public interface StringCipherer extends Cipherer<String, String> {
    @Override
    String encrypt(String text);

    @Override
    String decrypt(String encryptedText);
}
