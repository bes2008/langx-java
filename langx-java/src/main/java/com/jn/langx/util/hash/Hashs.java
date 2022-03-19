package com.jn.langx.util.hash;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.registry.Registry;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.ServiceLoader;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Hashs {

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
        hasher.setSeed(seed);
        hasher.update(bytes, 0, length);
        return hasher.getHash();
    }


    /**
     * Get a singleton instance of hash function of a given type.
     *
     * @param hasherName predefined hash type
     * @return hash function instance, or null if type is invalid
     */
    public static Hasher getInstance(String hasherName) {
        if ("murmur".equals(hasherName)) {
            hasherName = "murmur2";
        }
        Hasher factory = hasherFactoryRegistry.get(hasherName);
        if (factory != null) {
            return factory.get(0L);
        }
        if (hasherName.startsWith(HMacHasher.HASHER_NAME_PREFIX)) {
            String hmac = Strings.substring(hasherName, HMacHasher.HASHER_NAME_PREFIX.length());
            return new HMacHasher(hmac);
        }
        if (hasherName.startsWith(MessageDigestHasher.HASHER_NAME_PREFIX)) {
            String digestAlgorithm = Strings.substring(hasherName, MessageDigestHasher.HASHER_NAME_PREFIX.length());
            return new MessageDigestHasher(digestAlgorithm);
        }
        Hasher hasher = null;
        try {
            hasher = new HMacHasher(hasherName);
            return hasher;
        } catch (Throwable ex) {
        }
        try {
            hasher = new MessageDigestHasher(hasherName);
            return hasher;
        } catch (Throwable ex) {
        }
        throw new UnsupportedHashAlgorithmException(hasherName);
    }
}
