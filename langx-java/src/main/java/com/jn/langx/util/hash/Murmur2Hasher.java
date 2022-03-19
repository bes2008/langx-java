package com.jn.langx.util.hash;


/**
 * This is a very fast, non-cryptographic hash suitable for general hash-based
 * lookup.  See http://murmurhash.googlepages.com/ for more details.
 * <p>The C version of MurmurHash 2.0 found at that site was ported
 * to Java by Andrzej Bialecki (ab at getopt org).</p>
 * <p>
 * migrate from hadoop
 */
public class Murmur2Hasher extends AbstractHasher {
    private static Murmur2Hasher _instance = new Murmur2Hasher();

    public static Hasher getInstance() {
        return _instance;
    }

    private long h;

    public Murmur2Hasher(){
        reset();
    }

    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);
        this.h = seed;
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        byte[] bs = new byte[len];
        System.arraycopy(bytes, off, bs, 0, len);
        this.h = updateInternal(bs, bs.length, this.h);
    }

    @Override
    public long get() {
        long r = this.h;
        reset();
        return r;
    }

    private long updateInternal(byte[] data, int length, long initValue) {
        int m = 0x5bd1e995;
        int r = 24;

        long h = initValue ^ length;

        int len4 = length >> 2;

        for (int i = 0; i < len4; i++) {
            int i4 = i << 2;
            int k = data[i4 + 3];
            k = k << 8;
            k = k | (data[i4 + 2] & 0xff);
            k = k << 8;
            k = k | (data[i4 + 1] & 0xff);
            k = k << 8;
            k = k | (data[i4] & 0xff);
            k *= m;
            k ^= k >>> r;
            k *= m;
            h *= m;
            h ^= k;
        }

        // avoid calculating modulo
        int moduloLength = len4 << 2;
        int left = length - moduloLength;

        if (left != 0) {
            if (left >= 3) {
                h ^= data[length - 3] << 16;
            }
            if (left >= 2) {
                h ^= data[length - 2] << 8;
            }
            if (left >= 1) {
                h ^= data[length - 1];
            }

            h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

}
