package com.jn.langx.util.hash;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.registry.Registry;
import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.hash.streaming.HMacHasher;
import com.jn.langx.util.hash.streaming.MessageDigestHasher;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.ServiceLoader;

/**
 * @since 4.4.0
 */
public class Hashs {
    private static final Logger logger = Loggers.getLogger(Hashs.class);
    private final static Registry<String, Hasher> hasherFactoryRegistry;

    static {
        final GenericRegistry<Hasher> registry = new GenericRegistry<Hasher>();
        Collects.forEach(ServiceLoader.<Hasher>load(Hasher.class), new Consumer<Hasher>() {
            @Override
            public void accept(Hasher factory) {
                registry.register(factory);
            }
        });
        hasherFactoryRegistry = registry;
    }

    /**
     * Calculate a hash using all bytes from the input argument, and
     * a seed of 0.
     *
     * @param bytes input bytes
     * @return hash value
     */
    public static long hash(Hasher hasher, byte[] bytes) {
        return hash(hasher, bytes, bytes.length, 0);
    }

    /**
     * 一次性计算 hash
     * <p>
     * Calculate a hash using all bytes from the input argument,
     * and a provided seed value.
     *
     * @param bytes input bytes
     * @param seed  seed value
     * @return hash value
     */
    public static long hash(Hasher hasher, byte[] bytes, long seed) {
        return hash(hasher, bytes, bytes.length, seed);
    }

    /**
     * 一次性计算 hash
     * <p>
     * Calculate a hash using bytes from 0 to <code>length</code>, and
     * the provided seed value
     *
     * @param bytes  input bytes
     * @param length length of the valid bytes to consider
     * @param seed   seed value
     * @return hash value
     */
    public static long hash(Hasher hasher, byte[] bytes, int length, long seed) {
        return hasher.hash(bytes, length, seed);
    }

    public static long hash(String hasher, Object initParams, byte[] bytes, int length, long seed) {
        return getHasher(hasher, initParams).hash(bytes, length, seed);
    }

    /**
     * Get a singleton instance of hash function of a given type.
     *
     * @param hasherName predefined hash type
     * @return hash function instance, or null if type is invalid
     */
    public static <H extends Hasher> H getHasher(String hasherName, Object initParams) {
        if ("murmur".equals(hasherName)) {
            hasherName = "murmur2";
        }
        Hasher factory = hasherFactoryRegistry.get(Strings.lowerCase(hasherName));
        if (factory != null) {
            return (H)factory.get(initParams);
        }
        if (hasherName.startsWith(HMacHasher.HASHER_NAME_PREFIX)) {
            String hmac = Strings.substring(hasherName, HMacHasher.HASHER_NAME_PREFIX.length());
            Object params = new Object[]{hmac, initParams};
            return  (H)new HMacHasher().get(params);
        }
        if (hasherName.startsWith(MessageDigestHasher.HASHER_NAME_PREFIX)) {
            String digestAlgorithm = Strings.substring(hasherName, MessageDigestHasher.HASHER_NAME_PREFIX.length());
            return (H) new MessageDigestHasher(digestAlgorithm);
        }
        Hasher hasher = null;
        try {
            hasher = new HMacHasher(hasherName, (byte[]) initParams);
            return (H) hasher;
        } catch (SecurityException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof NoSuchAlgorithmException) {
                // ignore it
            } else {
                logger.error(ex.getMessage(), ex);
            }
        }
        try {
            hasher = new MessageDigestHasher(hasherName);
            return (H) hasher;
        } catch (SecurityException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof NoSuchAlgorithmException) {
                // ignore it
            } else {
                logger.error(ex.getMessage(), ex);
            }
        }
        throw new UnsupportedHashAlgorithmException(hasherName);
    }
}
