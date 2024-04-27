package com.jn.langx.security.pbe.cipher;

import com.jn.langx.security.crypto.cipher.BytesCipher;

public interface PBKDFCipher extends BytesCipher {
    @Override
    byte[] encrypt(byte[] text);

    @Override
    byte[] decrypt(byte[] encryptedText);
}
