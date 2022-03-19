package com.jn.langx.util.hash.streaming;


import com.jn.langx.util.hash.AbstractHasher;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @since 4.4.0
 */
public class Murmur3_128Hasher extends AdvancedStreamingHasher {
    private static final int CHUNK_SIZE = 16;
    private static final long C1 = 0x87c37b91114253d5L;
    private static final long C2 = 0x4cf5ad432745937fL;
    private long h1;
    private long h2;
    private int length;

    public Murmur3_128Hasher() {
        super(CHUNK_SIZE);
        setSeed(0);
    }

    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);
        this.h1 = seed;
        this.h2 = seed;
        this.length = 0;
    }


    @Override
    protected AbstractHasher createInstance() {
        return new Murmur3_128Hasher();
    }

    protected void process(ByteBuffer bb) {
        long k1 = bb.getLong();
        long k2 = bb.getLong();
        bmix64(k1, k2);
        length += CHUNK_SIZE;
    }

    private void bmix64(long k1, long k2) {
        h1 ^= mixK1(k1);

        h1 = Long.rotateLeft(h1, 27);
        h1 += h2;
        h1 = h1 * 5 + 0x52dce729;

        h2 ^= mixK2(k2);

        h2 = Long.rotateLeft(h2, 31);
        h2 += h1;
        h2 = h2 * 5 + 0x38495ab5;
    }

    private static final int UNSIGNED_MASK = 0xFF;

    private static int toInt(byte value) {
        return value & UNSIGNED_MASK;
    }

    protected void processRemaining(ByteBuffer bb) {
        long k1 = 0;
        long k2 = 0;
        length += bb.remaining();
        switch (bb.remaining()) {
            case 15:
                k2 ^= (long) toInt(bb.get(14)) << 48; // fall through
            case 14:
                k2 ^= (long) toInt(bb.get(13)) << 40; // fall through
            case 13:
                k2 ^= (long) toInt(bb.get(12)) << 32; // fall through
            case 12:
                k2 ^= (long) toInt(bb.get(11)) << 24; // fall through
            case 11:
                k2 ^= (long) toInt(bb.get(10)) << 16; // fall through
            case 10:
                k2 ^= (long) toInt(bb.get(9)) << 8; // fall through
            case 9:
                k2 ^= (long) toInt(bb.get(8)); // fall through
            case 8:
                k1 ^= bb.getLong();
                break;
            case 7:
                k1 ^= (long) toInt(bb.get(6)) << 48; // fall through
            case 6:
                k1 ^= (long) toInt(bb.get(5)) << 40; // fall through
            case 5:
                k1 ^= (long) toInt(bb.get(4)) << 32; // fall through
            case 4:
                k1 ^= (long) toInt(bb.get(3)) << 24; // fall through
            case 3:
                k1 ^= (long) toInt(bb.get(2)) << 16; // fall through
            case 2:
                k1 ^= (long) toInt(bb.get(1)) << 8; // fall through
            case 1:
                k1 ^= (long) toInt(bb.get(0));
                break;
            default:
                throw new AssertionError("Should never get here.");
        }
        h1 ^= mixK1(k1);
        h2 ^= mixK2(k2);
    }

    private static long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }

    private static long mixK1(long k1) {
        k1 *= C1;
        k1 = Long.rotateLeft(k1, 31);
        k1 *= C2;
        return k1;
    }

    private static long mixK2(long k2) {
        k2 *= C2;
        k2 = Long.rotateLeft(k2, 33);
        k2 *= C1;
        return k2;
    }

    protected long makeHash() {
        h1 ^= length;
        h2 ^= length;

        h1 += h2;
        h2 += h1;

        h1 = fmix64(h1);
        h2 = fmix64(h2);

        h1 += h2;
        h2 += h1;

        byte[] bytes = ByteBuffer.wrap(new byte[CHUNK_SIZE])
                .order(ByteOrder.LITTLE_ENDIAN)
                .putLong(h1)
                .putLong(h2)
                .array();
        return toLong(bytes);
    }

}
