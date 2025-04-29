package com.jn.langx.security.crypto.digest;


import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.digest.internal.Digest;
import com.jn.langx.security.crypto.digest.internal.Xof;

import java.security.MessageDigest;

public class LangxMessageDigestSpi extends MessageDigest implements DigestSizeAware {
    protected Digest delegate;
    /**
     * 产生的 digest 的长度，bytes
     */
    protected int digestSize;

    protected LangxMessageDigestSpi(Digest digest) {
        super(digest.getAlgorithmName());

        this.delegate = digest;
        this.digestSize = digest.getDigestSize();
    }

    protected LangxMessageDigestSpi(Xof digest, int outputSize) {
        super(digest.getAlgorithmName());

        this.delegate = digest;
        this.digestSize = Securitys.getBytesLength(outputSize);
    }

    public void engineReset() {
        delegate.reset();
    }

    public void engineUpdate(byte input) {
        delegate.update(input);
    }

    public void engineUpdate(byte[] input, int offset, int len) {
        delegate.update(input, offset, len);
    }

    @Override
    public int engineGetDigestLength() {
        return digestSize;
    }

    public byte[] engineDigest() {
        byte[] digestBytes = new byte[digestSize];

        delegate.doFinal(digestBytes, 0);

        return digestBytes;
    }

    @Override
    public int getDigestSize() {
        return delegate.getDigestSize();
    }

    public int getByteLength() {
        return delegate.getByteLength();
    }
}
