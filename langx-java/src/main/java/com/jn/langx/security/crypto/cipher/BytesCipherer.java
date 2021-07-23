package com.jn.langx.security.crypto.cipher;

public interface BytesCipherer extends Cipher<byte[], byte[]> {
    @Override
    byte[] encrypt(byte[] text);

    @Override
    byte[] decrypt(byte[] encryptedText);
}
