package com.jn.langx.security.crypto.cipher;

public interface BytesCipherer extends Cipherer<byte[], byte[]> {
    @Override
    byte[] encrypt(byte[] text);

    @Override
    byte[] decrypt(byte[] encryptedText);
}
