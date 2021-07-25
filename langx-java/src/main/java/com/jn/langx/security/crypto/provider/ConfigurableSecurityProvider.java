package com.jn.langx.security.crypto.provider;

import com.jn.langx.annotation.NonNull;

public interface ConfigurableSecurityProvider {
    boolean hasAlgorithm(String type, String name);

    void addAlgorithm(String key, Class spiClass);

    /**
     * @param key   configuration item name
     * @param value configuration item value
     */
    void addAlgorithm(String key, String value);

    /**
     * @param type      the SPI type: MessageDigest, KeyFactory, KeyGenerator, KeyAgreement, ...
     * @param oid       the object identifier
     * @param spiClassName the spi class name
     */
    void addAlgorithmOid(@NonNull String type, @NonNull String oid, @NonNull String spiClassName);

    void addAlias(String name, String alias);

    void addHmacAlgorithm(String digestAlgo, String hmacAlgoSpiClassName, String keyGenSpiClassName);

    void addHmacOidAlias(String hmacOid, String digestAlgorithm);

}
