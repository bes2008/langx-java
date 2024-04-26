package com.jn.langx.security.crypto.key.pb;

import java.security.NoSuchAlgorithmException;

public interface KDF {
    DerivedKey generate(String passphrase, byte[] saltBytes, int keyBitSize, int ivBitSize, int iterations, String hashAlgorithm) throws NoSuchAlgorithmException;
}