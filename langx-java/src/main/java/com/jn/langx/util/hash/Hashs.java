package com.jn.langx.util.hash;

public class Hashs {
    /**
     * Calculate a hash using all bytes from the input argument, and
     * a seed of -1.
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
}
