package com.jn.langx.security.crypto.cipher;

import com.jn.langx.util.function.Supplier0;

import java.util.List;

public interface CipherAlgorithmSuiteSupplier extends Supplier0<List<CipherAlgorithmSuite>> {
    @Override
    List<CipherAlgorithmSuite> get();
}
