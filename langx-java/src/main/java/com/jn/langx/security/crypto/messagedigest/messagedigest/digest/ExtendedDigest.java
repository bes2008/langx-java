package com.jn.langx.security.crypto.messagedigest.messagedigest.digest;

public interface ExtendedDigest extends Digest {
    /**
     * Return the size in bytes of the internal buffer the digest applies it's compression
     * function to.
     *
     * @return byte length of the digests internal buffer.
     */
    public int getByteLength();
}
