package com.jn.langx.security.crypto.digest.internal.impl;

import com.jn.langx.util.Memoable;
import com.jn.langx.util.io.bytes.Bytes;

public final class _SHA384Digest extends _AbstractLongDigest {
    private static final int DIGEST_LENGTH = 48;

    /**
     * Standard constructor
     */
    public _SHA384Digest() {
    }

    /**
     * Copy constructor.  This will copy the state of the provided
     * message digest.
     */
    public _SHA384Digest(_SHA384Digest t) {
        super(t);
    }

    /**
     * State constructor - create a digest initialised with the state of a previous one.
     *
     * @param encodedState the encoded state from the originating digest.
     */
    public _SHA384Digest(byte[] encodedState) {
        restoreState(encodedState);
    }

    public String getAlgorithmName() {
        return "SHA-384";
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    public int doFinal(byte[] out, int outOff) {
        finish();

        Bytes.longToBigEndian(H1, out, outOff);
        Bytes.longToBigEndian(H2, out, outOff + 8);
        Bytes.longToBigEndian(H3, out, outOff + 16);
        Bytes.longToBigEndian(H4, out, outOff + 24);
        Bytes.longToBigEndian(H5, out, outOff + 32);
        Bytes.longToBigEndian(H6, out, outOff + 40);

        reset();

        return DIGEST_LENGTH;
    }

    /**
     * reset the chaining variables
     */
    public void reset() {
        super.reset();

        /* SHA-384 initial hash value
         * The first 64 bits of the fractional parts of the square roots
         * of the 9th through 16th prime numbers
         */
        H1 = 0xcbbb9d5dc1059ed8L;
        H2 = 0x629a292a367cd507L;
        H3 = 0x9159015a3070dd17L;
        H4 = 0x152fecd8f70e5939L;
        H5 = 0x67332667ffc00b31L;
        H6 = 0x8eb44a8768581511L;
        H7 = 0xdb0c2e0d64f98fa7L;
        H8 = 0x47b5481dbefa4fa4L;
    }

    public Memoable copy() {
        return new _SHA384Digest(this);
    }

    public void reset(Memoable other) {
        _SHA384Digest d = (_SHA384Digest) other;

        super.copyIn(d);
    }

    public byte[] getEncodedState() {
        byte[] encoded = new byte[getEncodedStateSize()];
        super.populateState(encoded);
        return encoded;
    }
}
