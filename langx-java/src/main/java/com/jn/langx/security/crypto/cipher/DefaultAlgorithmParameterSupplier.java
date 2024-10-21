package com.jn.langx.security.crypto.cipher;

import com.jn.langx.annotation.Singleton;

import java.security.*;

/**
 * 基于 Provider 中注册的 Spec, Generator 来生成 参数
 *
 * @since 4.2.7
 */
@Singleton
public class DefaultAlgorithmParameterSupplier implements AlgorithmParameterSupplier {
    private static DefaultAlgorithmParameterSupplier INSTANCE;

    private DefaultAlgorithmParameterSupplier() {

    }

    public static DefaultAlgorithmParameterSupplier getInstance() {
        if (INSTANCE == null) {
            synchronized (DefaultAlgorithmParameterSupplier.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DefaultAlgorithmParameterSupplier();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
        Object parameters = null;
        try {
            if (provider != null) {
                parameters = AlgorithmParameters.getInstance(algorithm, provider);
            }
        } catch (NoSuchAlgorithmException ex) {
            // ignore it
        }
        return parameters;
    }
}
