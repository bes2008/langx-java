package com.jn.langx.util.bit;


import java.util.Arrays;

public final class BitVector implements Cloneable {
    private static final int BITSPERSLOT = 64;
    private static final int SLOTSQUANTA = 4;
    private static final int BITSHIFT = 6;
    private static final int BITMASK = 63;
    private long[] bits;

    public BitVector() {
        this.bits = new long[4];
    }

    public BitVector(long length) {
        int need = (int)growthNeeded(length);
        this.bits = new long[need];
    }

    public BitVector(long[] bits) {
        this.bits = bits.clone();
    }

    public void copy(BitVector other) {
        this.bits = other.bits.clone();
    }

    private static long slotsNeeded(long length) {
        return length + 63L >> 6;
    }

    private static long growthNeeded(long length) {
        return (slotsNeeded(length) + 4L - 1L) / 4L * 4L;
    }

    private long slot(int index) {
        return 0 <= index && index < this.bits.length ? this.bits[index] : 0L;
    }

    public void resize(long length) {
        int need = (int)growthNeeded(length);
        if (this.bits.length != need) {
            this.bits = Arrays.copyOf(this.bits, need);
        }

        int shift = (int)(length & 63L);
        int slot = (int)(length >> 6);
        if (shift != 0) {
            long[] var10000 = this.bits;
            var10000[slot] &= (1L << shift) - 1L;
            ++slot;
        }

        while(slot < this.bits.length) {
            this.bits[slot] = 0L;
            ++slot;
        }

    }

    public void set(long bit) {
        long[] var10000 = this.bits;
        var10000[(int)(bit >> 6)] |= 1L << (int)(bit & 63L);
    }

    public void clear(long bit) {
        long[] var10000 = this.bits;
        var10000[(int)(bit >> 6)] &= ~(1L << (int)(bit & 63L));
    }

    public void toggle(long bit) {
        long[] var10000 = this.bits;
        var10000[(int)(bit >> 6)] ^= 1L << (int)(bit & 63L);
    }

    public void setTo(long length) {
        if (0L < length) {
            int lastWord = (int)(length >> 6);
            long lastBits = (1L << (int)(length & 63L)) - 1L;
            Arrays.fill(this.bits, 0, lastWord, -1L);
            if (lastBits != 0L) {
                long[] var10000 = this.bits;
                var10000[lastWord] |= lastBits;
            }
        }

    }

    public void clearAll() {
        Arrays.fill(this.bits, 0L);
    }

    public boolean isSet(long bit) {
        return (this.bits[(int)(bit >> 6)] & 1L << (int)(bit & 63L)) != 0L;
    }

    public boolean isClear(long bit) {
        return (this.bits[(int)(bit >> 6)] & 1L << (int)(bit & 63L)) == 0L;
    }

    public void shiftLeft(long shift, long length) {
        if (shift != 0L) {
            int leftShift = (int)(shift & 63L);
            int rightShift = 64 - leftShift;
            int slotShift = (int)(shift >> 6);
            int slotCount = this.bits.length - slotShift;
            int slot;
            int from;
            if (leftShift == 0) {
                slot = 0;

                for(from = slotShift; slot < slotCount; ++from) {
                    this.bits[slot] = this.slot(from);
                    ++slot;
                }
            } else {
                slot = 0;

                for(from = slotShift; slot < slotCount; ++slot) {
                    long[] var10000 = this.bits;
                    long var10002 = this.slot(from) >>> leftShift;
                    ++from;
                    var10000[slot] = var10002 | this.slot(from) << rightShift;
                }
            }
        }

        this.resize(length);
    }

    public void shiftRight(long shift, long length) {
        this.resize(length);
        if (shift != 0L) {
            int rightShift = (int)(shift & 63L);
            int leftShift = 64 - rightShift;
            int slotShift = (int)(shift >> 6);
            int slot;
            int from;
            slot = this.bits.length;

            for(from = slot - slotShift; slot > 0; this.bits[slot] = this.slot(from - 1) >>> leftShift | this.slot(from) << rightShift) {
                --slot;
                --from;
            }
        }

        this.resize(length);
    }

    public void setRange(long fromIndex, long toIndex) {
        if (fromIndex < toIndex) {
            int firstWord = (int)(fromIndex >> 6);
            int lastWord = (int)(toIndex - 1L >> 6);
            long firstBits = -1L << (int)fromIndex;
            long lastBits = -1L >>> (int)(-toIndex);
            long[] var10000;
            if (firstWord == lastWord) {
                var10000 = this.bits;
                var10000[firstWord] |= firstBits & lastBits;
            } else {
                var10000 = this.bits;
                var10000[firstWord] |= firstBits;
                Arrays.fill(this.bits, firstWord + 1, lastWord, -1L);
                var10000 = this.bits;
                var10000[lastWord] |= lastBits;
            }
        }

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
