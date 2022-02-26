package com.jn.langx.security.gm.crypto.bc.symmetric.sm4;

import com.jn.langx.security.crypto.cipher.CipherAlgorithmSuite;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmSuiteSupplier;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class Sm4AlgorithmSuiteSupplier implements CipherAlgorithmSuiteSupplier {
    @Override
    public List<CipherAlgorithmSuite> get() {
        return Collects.asList(
                new CipherAlgorithmSuite("SM4", "SM4/CBC/PKCS7Padding", new SM4AlgorithmSpecSupplier(GmService.SM4_IV_DEFAULT), 128)
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
