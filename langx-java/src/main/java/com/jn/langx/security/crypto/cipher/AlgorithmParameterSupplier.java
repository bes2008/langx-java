package com.jn.langx.security.crypto.cipher;

import java.security.Provider;
import java.security.SecureRandom;

/**
 * 返回值类型可以是：
 * @see java.security.spec.AlgorithmParameterSpec
 * @see java.security.AlgorithmParameters
 */
public interface AlgorithmParameterSupplier {
    public Object get(String algorithm, String transform, Provider provider, SecureRandom secureRandom);
}
