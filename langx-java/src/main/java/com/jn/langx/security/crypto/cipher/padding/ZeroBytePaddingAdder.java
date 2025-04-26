package com.jn.langx.security.crypto.cipher.padding;

import java.security.SecureRandom;


/**
 * A padder that adds NULL byte padding to a block.
 */
public class ZeroBytePaddingAdder implements BlockCipherPaddingAdder {
    /**
     * Initialise the padder.
     *
     * @param random - a SecureRandom if available.
     */
    public void init(SecureRandom random)
            throws IllegalArgumentException {
        // nothing to do.
    }

    /**
     * Return the name of the algorithm the padder implements.
     *
     * @return the name of the algorithm the padder implements.
     */
    public String getPaddingName() {
        return "ZeroByte";
    }

    /**
     * add the pad bytes to the passed in block, returning the
     * number of bytes added.
     */
    public int addPadding(
            byte[] in,
            int inOff) {
        int added = (in.length - inOff);

        while (inOff < in.length) {
            in[inOff] = (byte) 0;
            inOff++;
        }

        return added;
    }

    /**
     * return the number of pad bytes present in the block.
     */
    public int padCount(byte[] in) throws PaddingException {
        int count = in.length;

        while (count > 0) {
            if (in[count - 1] != 0) {
                break;
            }

            count--;
        }

        return in.length - count;
    }
}