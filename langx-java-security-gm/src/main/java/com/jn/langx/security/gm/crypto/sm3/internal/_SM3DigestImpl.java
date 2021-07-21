package com.jn.langx.security.gm.crypto.sm3.internal;


import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.util.Pack;

public class _SM3DigestImpl extends GeneralDigest
{
    private static final int DIGEST_LENGTH = 32;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int H8;
    private int[] word;
    private int wordOff;

    public _SM3DigestImpl() {
        this.word = new int[68];
        this.reset();
    }

    @Override
    protected void processBlock() {
        for (int i = 16; i <= 67; ++i) {
            this.word[i] = (this.P1(this.word[i - 16] ^ this.word[i - 9] ^ this.cycleShiftLeft(this.word[i - 3], 15)) ^ this.cycleShiftLeft(this.word[i - 13], 7) ^ this.word[i - 6]);
        }
        final int[] array = new int[64];
        for (int j = 0; j <= 63; ++j) {
            array[j] = (this.word[j] ^ this.word[j + 4]);
        }
        int h1 = this.H1;
        int h2 = this.H2;
        int n = this.H3;
        int h3 = this.H4;
        int n2 = this.H5;
        int h4 = this.H6;
        int n3 = this.H7;
        int h5 = this.H8;
        for (int k = 0; k < 64; ++k) {
            final int cycleShiftLeft = this.cycleShiftLeft(this.cycleShiftLeft(h1, 12) + n2 + this.cycleShiftLeft(this.T(k), k), 7);
            final int n4 = this.FF(h1, h2, n, k) + h3 + (cycleShiftLeft ^ this.cycleShiftLeft(h1, 12)) + array[k];
            final int n5 = this.GG(n2, h4, n3, k) + h5 + cycleShiftLeft + this.word[k];
            h3 = n;
            n = this.cycleShiftLeft(h2, 9);
            h2 = h1;
            h1 = n4;
            h5 = n3;
            n3 = this.cycleShiftLeft(h4, 19);
            h4 = n2;
            n2 = this.P0(n5);
        }
        this.H1 ^= h1;
        this.H2 ^= h2;
        this.H3 ^= n;
        this.H4 ^= h3;
        this.H5 ^= n2;
        this.H6 ^= h4;
        this.H7 ^= n3;
        this.H8 ^= h5;
        this.wordOff = 0;
        for (int l = 0; l != this.word.length; ++l) {
            this.word[l] = 0;
        }
    }

    @Override
    protected void processLength(final long n) {
        if (this.wordOff > 14) {
            this.processBlock();
        }
        this.word[14] = (int)(n >>> 32);
        this.word[15] = (int)(n & -1L);
    }

    @Override
    protected void processWord(final byte[] array, int n) {
        this.word[this.wordOff] = (array[n] << 24 | (array[++n] & 0xFF) << 16 | (array[++n] & 0xFF) << 8 | (array[++n] & 0xFF));
        if (++this.wordOff == 16) {
            this.processBlock();
        }
    }

    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        Pack.intToBigEndian(this.H1, array, n);
        Pack.intToBigEndian(this.H2, array, n + 4);
        Pack.intToBigEndian(this.H3, array, n + 8);
        Pack.intToBigEndian(this.H4, array, n + 12);
        Pack.intToBigEndian(this.H5, array, n + 16);
        Pack.intToBigEndian(this.H6, array, n + 20);
        Pack.intToBigEndian(this.H7, array, n + 24);
        Pack.intToBigEndian(this.H8, array, n + 28);
        this.reset();
        return 32;
    }

    @Override
    public String getAlgorithmName() {
        return "SM3";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = 1937774191;
        this.H2 = 1226093241;
        this.H3 = 388252375;
        this.H4 = -628488704;
        this.H5 = -1452330820;
        this.H6 = 372324522;
        this.H7 = -477237683;
        this.H8 = -1325724082;
        this.wordOff = 0;
        for (int i = 0; i != this.word.length; ++i) {
            this.word[i] = 0;
        }
    }

    public int FF(final int n, final int n2, final int n3, final int n4) {
        if (n4 >= 0 && n4 <= 15) {
            return n ^ n2 ^ n3;
        }
        if (n4 >= 16 && n4 <= 63) {
            return (n & n2) | (n & n3) | (n2 & n3);
        }
        throw new RuntimeException("\u7d22\u5f15\u8d8a\u754c");
    }

    public int GG(final int n, final int n2, final int n3, final int n4) {
        if (n4 >= 0 && n4 <= 15) {
            return n ^ n2 ^ n3;
        }
        if (n4 >= 16 && n4 <= 63) {
            return (n & n2) | (~n & n3);
        }
        throw new RuntimeException("\u7d22\u5f15\u8d8a\u754c");
    }

    public int P0(final int n) {
        return n ^ this.cycleShiftLeft(n, 9) ^ this.cycleShiftLeft(n, 17);
    }

    public int P1(final int n) {
        return n ^ this.cycleShiftLeft(n, 15) ^ this.cycleShiftLeft(n, 23);
    }

    public int T(final int n) {
        if (n >= 0 && n <= 15) {
            return 2043430169;
        }
        if (n >= 16 && n <= 63) {
            return 2055708042;
        }
        throw new RuntimeException("\u7d22\u5f15\u8d8a\u754c\uff1a" + n);
    }

    private int cycleShiftLeft(final int n, final int n2) {
        return n << n2 | n >>> 32 - n2;
    }

}
