package com.jn.langx.security.crypto.cipher;

import com.jn.langx.annotation.Singleton;

import java.security.*;

/**
 * 基于 Provider 中注册的 Spec, Generator 来生成 参数
 */
@Singleton
public class DefaultAlgorithmParameterSupplier implements AlgorithmParameterSupplier{
    public static final DefaultAlgorithmParameterSupplier INSTANCE= new DefaultAlgorithmParameterSupplier();
    private DefaultAlgorithmParameterSupplier(){

    }

    @Override
    public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
        Object parameters=null;
        try {
            parameters = AlgorithmParameters.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException ex) {
            // ignore it
        }
        return parameters;
    }
}
