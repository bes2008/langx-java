package com.jn.langx.security.pbe.cipher.kdf;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 基于一个 password 来生成 secret key
 */
public interface PBKDF {
    byte[] genSalt(SecureRandom secureRandom, int saltBitSize, int round);
    DerivedKey generate(String password, byte[] saltBytes, int keyBitSize, int ivBitSize, int iterations, String hashAlgorithm) throws NoSuchAlgorithmException;
}
