package com.jn.langx.security.crypto.cipher;

import com.jn.langx.Ordered;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class BuiltinCipherAlgorithmSuiteSupplier implements CipherAlgorithmSuiteSupplier {
    private static final List<CipherAlgorithmSuite> SUITES = Collects.immutableArrayList(
            new CipherAlgorithmSuite("AES", "AES/ECB/PKCS5Padding"),
            new CipherAlgorithmSuite("SM2", "SM2"),
            new CipherAlgorithmSuite("SM4", "SM4/CBC/PKCS7Padding"),
            new CipherAlgorithmSuite("RSA", "RSA/ECB/PKCS1Padding")
    );

    @Override
    public List<CipherAlgorithmSuite> get() {
        return SUITES;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
