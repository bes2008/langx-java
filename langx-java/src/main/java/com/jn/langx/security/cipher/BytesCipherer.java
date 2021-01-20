package com.jn.langx.security.cipher;

public interface BytesCipherer extends Cipherer<byte[], byte[]> {
    @Override
    byte[] encrypt(byte[] text);

    @Override
    byte[] decrypt(byte[] encryptedText);
}
