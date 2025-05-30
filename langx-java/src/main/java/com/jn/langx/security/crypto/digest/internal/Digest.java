package com.jn.langx.security.crypto.digest.internal;

import com.jn.langx.security.crypto.digest.DigestSizeAware;

/**
 * interface that a message digest conforms to.
 * <p>
 * 这是一套自定义的 digest 规范，跟官方接口 MessageDigest 没有关系
 */
public interface Digest extends DigestSizeAware {
    /**
     * return the algorithm name
     *
     * @return the algorithm name
     */
    String getAlgorithmName();

    /**
     * return the size, in bytes, of the digest produced by this message digest.
     *
     * @return the size, in bytes, of the digest produced by this message digest.
     */
    int getDigestSize();

    /**
     * Return the size in bytes of the internal buffer the digest applies it's compression
     * function to.
     *
     * @return byte length of the digests internal buffer.
     */
    int getByteLength();

    /**
     * update the message digest with a single byte.
     *
     * @param in the input byte to be entered.
     */
    void update(byte in);

    /**
     * update the message digest with a block of bytes.
     *
     * @param in    the byte array containing the data.
     * @param inOff the offset into the byte array where the data starts.
     * @param len   the length of the data.
     */
    void update(byte[] in, int inOff, int len);

    /**
     * close the digest, producing the final digest value. The doFinal
     * call leaves the digest reset.
     *
     * @param out    the array the digest is to be copied into.
     * @param outOff the offset into the out array the digest is to start at.
     */
    int doFinal(byte[] out, int outOff);

    /**
     * reset the digest back to it's initial state.
     */
    void reset();
}
