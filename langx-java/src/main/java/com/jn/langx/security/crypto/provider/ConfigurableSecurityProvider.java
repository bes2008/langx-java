package com.jn.langx.security.crypto.provider;

import com.jn.langx.annotation.NonNull;

/**
 * Interface for a configurable security provider, allowing the addition and configuration of security algorithms.
 */
public interface ConfigurableSecurityProvider {

    /**
     * Checks if a security algorithm of a given type and name exists.
     *
     * @param type The type of security algorithm, such as "MessageDigest" or "KeyFactory".
     * @param name The name of the security algorithm, such as "SHA-256".
     * @return true if the algorithm exists; otherwise, false.
     */
    boolean hasAlgorithm(String type, String name);

    /**
     * Adds a security algorithm by specifying its key and SPI (Service Provider Interface) class.
     *
     * @param key The key identifying the algorithm.
     * @param spiClass The SPI class of the algorithm.
     */
    void addAlgorithm(String key, Class spiClass);

    /**
     * Adds a security algorithm by specifying its key and value.
     *
     * @param key The key identifying the algorithm.
     * @param value The value associated with the algorithm.
     */
    void addAlgorithm(String key, String value);

    /**
     * Adds a security algorithm with an object identifier (OID).
     *
     * @param type The SPI type: MessageDigest, KeyFactory, KeyGenerator, KeyAgreement, etc.
     * @param oid The object identifier for the algorithm.
     * @param spiClassName The SPI class name for the algorithm.
     */
    void addAlgorithmOid(@NonNull String type, @NonNull String oid, @NonNull String spiClassName);

    /**
     * Adds an alias for an algorithm name.
     *
     * @param name The original name of the algorithm.
     * @param alias An alias for the algorithm.
     */
    void addAlias(String name, String alias);

    /**
     * Adds an HMAC (Hash-Based Message Authentication Code) algorithm by specifying the digest algorithm, HMAC algorithm SPI class, and key generator SPI class.
     *
     * @param digestAlgo The digest algorithm, such as "SHA-256".
     * @param hmacAlgoSpiClass The SPI class for the HMAC algorithm.
     * @param keyGenSpiClass The SPI class for the key generator.
     */
    void addHmacAlgorithm(String digestAlgo, Class hmacAlgoSpiClass, Class keyGenSpiClass);

    /**
     * Adds an HMAC algorithm by specifying the digest algorithm, HMAC algorithm SPI class name, and key generator SPI class name.
     *
     * @param digestAlgo The digest algorithm, such as "SHA-256".
     * @param hmacAlgoSpiClassName The SPI class name for the HMAC algorithm.
     * @param keyGenSpiClassName The SPI class name for the key generator.
     */
    void addHmacAlgorithm(String digestAlgo, String hmacAlgoSpiClassName, String keyGenSpiClassName);

    /**
     * Adds an OID alias for an HMAC algorithm.
     *
     * @param hmacOid The object identifier for the HMAC algorithm.
     * @param digestAlgorithm The corresponding digest algorithm.
     */
    void addHmacOidAlias(String hmacOid, String digestAlgorithm);

}

