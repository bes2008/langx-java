package com.jn.langx.security.pbe.cipher;

import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public interface PBKDFCipher {
    void init(PBEKeySpec parameterSpec, SecureRandom secureRandom);

    void init(PBEKey parameterSpec, SecureRandom secureRandom);

    byte[] encrypt(byte[] message);
    byte[] decrypt(byte[] encryptedText);
}
