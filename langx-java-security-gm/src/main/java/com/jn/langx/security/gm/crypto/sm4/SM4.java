package com.jn.langx.security.gm.crypto.sm4;

import com.jn.langx.security.crypto.CryptoException;
import com.jn.langx.util.Bytes;

import javax.crypto.*;

/**
 * SM4 对称加密算法本身，不对外公开使用
 *
 */
class SM4
{
    private byte[][] sboxTable;
    private int[] CK;
    private static int[] FK;
    private int[] rk;
    private int[] rrk;
    public static int BLOCK_SIZE;
    public static final int ECB_MODE = 0;
    public static final int CBC_MODE = 1;
    private int mode;
    private int opMode;
    private byte[] iv;

    static {
        SM4.FK = new int[] { -1548633402, 1453994832, 1736282519, -1301273892 };
        SM4.BLOCK_SIZE = 16;
    }

    public SM4() {
        this.sboxTable = new byte[][] { { -42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5 }, { 43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103 }, { -100, 66, 80, -12, -111, -17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98 }, { -28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108, -6, 117, -113, 63, -90 }, { 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88 }, { 104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53 }, { 30, 36, 14, 94, 99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 120, -121 }, { -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98 }, { -22, -65, -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95 }, { -32, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 48, -11, -116, -79, -29 }, { 29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111 }, { -43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81 }, { -115, 27, -81, -110, -69, -35, -68, 127, 17, -39, 92, 65, 31, 16, 90, -40 }, { 10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27, -76, -80 }, { -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124 }, { 24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72 } };
        this.CK = new int[] { 462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257 };
        this.rk = new int[32];
        this.rrk = null;
        this.mode = 0;
        this.opMode = 1;
        this.iv = null;
    }

    public void setKey(final byte[] array) throws CryptoException {
        if (array.length != SM4.BLOCK_SIZE) {
            throw new CryptoException("unsupported key size");
        }
        final int[] array2 = new int[4];
        for (int i = 0; i < 4; ++i) {
            array2[i] = Bytes.bigEndianToInt(array, i * 4);
        }
        this.keyExpension(array2);
    }

    public void setIv(final byte[] iv) {
        this.iv = iv;
    }

    public byte[] getIv() {
        return this.iv;
    }

    public void setMode(final int mode) {
        this.mode = mode;
    }

    public void setOpMode(final int opMode) {
        this.opMode = opMode;
    }

    public int getOpMode() {
        return this.opMode;
    }

    public int encrypt(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws IllegalBlockSizeException {
        if (n2 % SM4.BLOCK_SIZE != 0) {
            throw new IllegalBlockSizeException();
        }
        final int n4 = n2 / SM4.BLOCK_SIZE;
        switch (this.mode) {
            case 0: {
                for (int i = 0; i < n4; ++i) {
                    this.encrytBlock(array, n + SM4.BLOCK_SIZE * i, array2, n3 + SM4.BLOCK_SIZE * i);
                }
                break;
            }
            case 1: {
                if (this.opMode == 1) {
                    final byte[] iv = this.iv;
                    for (int j = 0; j < n4; ++j) {
                        for (int k = 0; k < SM4.BLOCK_SIZE; ++k) {
                            final byte[] array3 = iv;
                            final int n5 = k;
                            array3[n5] ^= array[n + SM4.BLOCK_SIZE * j + k];
                        }
                        this.encrytBlock(iv, 0, array2, n3 + SM4.BLOCK_SIZE * j);
                        for (int l = 0; l < SM4.BLOCK_SIZE; ++l) {
                            iv[l] = array2[n3 + SM4.BLOCK_SIZE * j + l];
                        }
                    }
                    break;
                }
                final byte[] iv2 = this.iv;
                final byte[] array4 = new byte[SM4.BLOCK_SIZE];
                for (int n6 = 0; n6 < n4; ++n6) {
                    final int n7 = n + SM4.BLOCK_SIZE * n6;
                    final int n8 = n3 + SM4.BLOCK_SIZE * n6;
                    System.arraycopy(array, n7, array4, 0, SM4.BLOCK_SIZE);
                    this.encrytBlock(array, n7, array2, n8);
                    for (int n9 = 0; n9 < SM4.BLOCK_SIZE; ++n9) {
                        final int n10 = n3 + SM4.BLOCK_SIZE * n6 + n9;
                        array2[n10] ^= iv2[n9];
                    }
                    System.arraycopy(array4, 0, iv2, 0, SM4.BLOCK_SIZE);
                }
                break;
            }
            default: {
                for (int n11 = 0; n11 < n4; ++n11) {
                    this.encrytBlock(array, n + SM4.BLOCK_SIZE * n11, array2, n3 + SM4.BLOCK_SIZE * n11);
                }
                break;
            }
        }
        return n2;
    }

    public void encrytBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] array3 = new int[4];
        for (int i = 0; i < array3.length; ++i) {
            array3[i] = Bytes.bigEndianToInt(array, i * 4 + n);
        }
        final int[] r = this.R(array3);
        for (int j = 0; j < 4; ++j) {
            Bytes.intToBigEndian(r[j], array2, n2 + j * 4);
        }
    }

    private byte sbox(final int n) {
        return this.sboxTable[n >> 4 & 0xF][n & 0xF];
    }

    private int NL(final int n) {
        return Bytes.bigEndianToInt(this.sbox(n >> 24 & 0xFF), this.sbox(n >> 16 & 0xFF), this.sbox(n >> 8 & 0xFF), this.sbox(n & 0xFF));
    }

    private int L(final int n) {
        return n ^ Integer.rotateLeft(n, 2) ^ Integer.rotateLeft(n, 10) ^ Integer.rotateLeft(n, 18) ^ Integer.rotateLeft(n, 24);
    }

    private int T(final int n) {
        return this.L(this.NL(n));
    }

    private int L2(final int n) {
        return n ^ Integer.rotateLeft(n, 13) ^ Integer.rotateLeft(n, 23);
    }

    private int T2(final int n) {
        return this.L2(this.NL(n));
    }

    private int[] keyExpension(final int[] array) {
        final int[] array2 = new int[36];
        for (int i = 0; i < 4; ++i) {
            array2[i] = (array[i] ^ SM4.FK[i]);
        }
        for (int j = 0; j < this.rk.length; ++j) {
            array2[j + 4] = (array2[j] ^ this.T2(array2[j + 1] ^ array2[j + 2] ^ array2[j + 3] ^ this.CK[j]));
            this.rk[j] = array2[j + 4];
        }
        return array2;
    }

    private int[] R(final int[] array) {
        int[] array2 = this.rk;
        if (this.opMode == 2) {
            if (this.rrk == null) {
                this.rrk = new int[32];
                for (int i = 0; i < array2.length; ++i) {
                    this.rrk[i] = array2[array2.length - 1 - i];
                }
            }
            array2 = this.rrk;
        }
        final int[] array3 = new int[36];
        array3[0] = array[0];
        array3[1] = array[1];
        array3[2] = array[2];
        array3[3] = array[3];
        for (int j = 0; j < array2.length; ++j) {
            array3[j + 4] = this.F(array3[j], array3[j + 1], array3[j + 2], array3[j + 3], array2[j]);
        }
        return new int[] { array3[35], array3[34], array3[33], array3[32] };
    }

    private int F(final int n, final int n2, final int n3, final int n4, final int n5) {
        return n ^ this.T(n2 ^ n3 ^ n4 ^ n5);
    }
}
