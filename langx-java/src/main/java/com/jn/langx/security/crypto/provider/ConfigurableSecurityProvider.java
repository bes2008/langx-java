package com.jn.langx.security.crypto.provider;

import com.jn.langx.annotation.NonNull;

public interface ConfigurableSecurityProvider {
    boolean hasAlgorithm(String type, String name);

    /**
     *
     * @param key  configuration item name
     * @param value configuration item value
     */
    void addAlgorithm(String key, String value);

    /**
     *
     * @param type the SPI type: MessageDigest, KeyFactory, KeyGenerator, KeyAgreement, ...
     * @param oid the object identifier
     * @param className the spi class name
     */
    void addAlgorithmOid(@NonNull String type, @NonNull String oid, @NonNull String className);

}
