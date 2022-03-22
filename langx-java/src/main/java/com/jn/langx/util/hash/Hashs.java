package com.jn.langx.util.hash;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.registry.Registry;
import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.hash.streaming.HMacHasher;
import com.jn.langx.util.hash.streaming.MessageDigestHasher;
import com.jn.langx.util.hash.streaming.crc.CRCs;
import com.jn.langx.util.hash.streaming.crc.CrcHasher;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ServiceLoader;

import static com.hazelcast.internal.util.Preconditions.checkPositive;
import static java.lang.Math.abs;

/**
 * @since 4.4.0
 */
public class Hashs {
    private static final Logger logger = Loggers.getLogger(Hashs.class);
    private final static Registry<String, Hasher> hasherFactoryRegistry;

    static {
        final GenericRegistry<Hasher> registry = new GenericRegistry<Hasher>(Collects.<String, Hasher>emptyHashMap(true));
        registry.init();
        Collects.forEach(ServiceLoader.<Hasher>load(Hasher.class), new Consumer<Hasher>() {
            @Override
            public void accept(Hasher factory) {
                registry.register(factory);
            }
        });
        hasherFactoryRegistry = registry;

        // CRC
        List<String> crcNames = CRCs.getCrcNames();
        Collects.forEach(crcNames, new Consumer<String>() {
            @Override
            public void accept(String name) {
                hasherFactoryRegistry.register(Strings.lowerCase(name), new CrcHasher(name));
            }
        });

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
            return (H) factory.get(initParams);
        }

        if (hasherName.startsWith(HMacHasher.HASHER_NAME_PREFIX)) {
            String hmac = Strings.substring(hasherName, HMacHasher.HASHER_NAME_PREFIX.length());
            Object params = new Object[]{hmac, initParams};
            return (H) new HMacHasher().get(params);
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

    /**
     * A function that calculates the index (e.g. to be used in an array/list) for a given hash. The returned value will always
     * be equal or larger than 0 and will always be smaller than 'length'.
     *
     * The reason this function exists is to deal correctly with negative and especially the Integer.MIN_VALUE; since that can't
     * be used safely with a Math.abs function.
     *
     * @param length the length of the array/list
     * @return the mod of the hash
     * @throws IllegalArgumentException if length is smaller than 1.
     */
    public static int hashToIndex(int hash, int length) {
        checkPositive("length", length);

        if (hash == Integer.MIN_VALUE) {
            return 0;
        }

        return abs(hash) % length;
    }
}
