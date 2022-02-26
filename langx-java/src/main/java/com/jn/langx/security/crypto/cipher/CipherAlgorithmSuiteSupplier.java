package com.jn.langx.security.crypto.cipher;

import com.jn.langx.Ordered;
import com.jn.langx.util.function.Supplier0;

import java.util.List;

/**
 * @since 4.2.7
 */
public interface CipherAlgorithmSuiteSupplier extends Supplier0<List<CipherAlgorithmSuite>>, Ordered {
    @Override
    List<CipherAlgorithmSuite> get();
}
