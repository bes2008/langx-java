package com.jn.langx.security.crypto.cipher.pb;

import com.jn.langx.security.crypto.cipher.BytesCipher;

public interface PBCipher extends BytesCipher {
    @Override
    byte[] encrypt(byte[] text);

    @Override
    byte[] decrypt(byte[] encryptedText);
}
