package com.jn.langx.util.hash;


import com.google.common.base.Preconditions;

public class Murmur3_32Hasher extends Hasher {
    private static final int CHUNK_SIZE = 4;

    @Override
    public void update(byte[] input, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        int h1 = Long.valueOf(seed).intValue();
        int i;
        for (i = 0; i + CHUNK_SIZE <= len; i += CHUNK_SIZE) {
            int k1 = mixK1(getIntLittleEndian(input, off + i));
            h1 = mixH1(h1, k1);
        }

        int k1 = 0;
        for (int shift = 0; i < len; i++, shift += 8) {
            k1 ^= toInt(input[off + i]) << shift;
        }
        h1 ^= mixK1(k1);
        this.h = fmix(h1, len);
    }

    private int h;

    // Finalization mix - force all bits of a hash block to avalanche
    private static int fmix(int h1, int length) {
        h1 ^= length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;
        return h1;
    }

    private static final int UNSIGNED_MASK = 0xFF;

    public static int toInt(byte value) {
        return value & UNSIGNED_MASK;
    }

    private static final int C1 = 0xcc9e2d51;
    private static final int C2 = 0x1b873593;

    private static int getIntLittleEndian(byte[] input, int offset) {
        return fromBytes(input[offset + 3], input[offset + 2], input[offset + 1], input[offset]);
    }

    /**
     * Returns the {@code int} value whose byte representation is the given 4 bytes, in big-endian
     * order; equivalent to {@code Ints.fromByteArray(new byte[] {b1, b2, b3, b4})}.
     */
    private static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    private static int mixK1(int k1) {
        k1 *= C1;
        k1 = Integer.rotateLeft(k1, 15);
        k1 *= C2;
        return k1;
    }

    private static int mixH1(int h1, int k1) {
        h1 ^= k1;
        h1 = Integer.rotateLeft(h1, 13);
        h1 = h1 * 5 + 0xe6546b64;
        return h1;
    }

    @Override
    protected void update(byte b) {

    }

    @Override
    public long get() {
        return this.h;
    }
}
