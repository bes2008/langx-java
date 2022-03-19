package com.jn.langx.util.hash;

import com.jn.langx.util.Strings;

import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Hashs {
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
        return hasher.get();
    }



    /**
     * Get a singleton instance of hash function of a given type.
     *
     * @param hasherName predefined hash type
     * @return hash function instance, or null if type is invalid
     */
    public static Hasher getInstance(String hasherName) {
        if ("jenkins".equals(hasherName)) {
            return new JenkinsHasher();
        }
        if ("murmur".equals(hasherName) || "murmur2".equals(hasherName)) {
            return new Murmur2Hasher();
        }
        if ("murmur3_32".equals(hasherName)) {
            return new Murmur3_32Hasher();
        }
        if ("murmur3_128".equals(hasherName)) {
            return new Murmur3_128Hasher();
        }
        if ("crc32c".equals(hasherName)) {
            return new Crc32cHasher();
        }
        if (hasherName.startsWith(HMacHasher.HASHER_NAME_PREFIX)) {
            String hmac = Strings.substring(hasherName, HMacHasher.HASHER_NAME_PREFIX.length());
            return new HMacHasher(hmac);
        }
        if (hasherName.startsWith(MessageDigestHasher.HASHER_NAME_PREFIX)) {
            String digestAlgorithm = Strings.substring(hasherName, MessageDigestHasher.HASHER_NAME_PREFIX.length());
            return new MessageDigestHasher(digestAlgorithm);
        }
        if ("adler32".equals(hasherName)) {
            Checksum checksum = new Adler32();
            return new ChecksumHasher(checksum);
        }
        if ("crc32".equals(hasherName)) {
            Checksum checksum = new CRC32();
            return new ChecksumHasher(checksum);
        }
        throw new UnsupportedHashAlgorithmException(hasherName);
    }
}
