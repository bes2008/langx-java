package com.jn.langx.security.messagedigest;


import com.jn.langx.security.messagedigest.digest.Digest;
import com.jn.langx.security.messagedigest.digest.Xof;

import java.security.MessageDigest;

public class LangxMessageDigest extends MessageDigest {
    protected Digest delegate;
    protected int digestSize;

    protected LangxMessageDigest(Digest digest) {
        super(digest.getAlgorithmName());

        this.delegate = digest;
        this.digestSize = digest.getDigestSize();
    }

    protected LangxMessageDigest(Xof digest, int outputSize) {
        super(digest.getAlgorithmName());

        this.delegate = digest;
        this.digestSize = outputSize / 8;
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

    public int engineGetDigestLength() {
        return digestSize;
    }

    public byte[] engineDigest() {
        byte[] digestBytes = new byte[digestSize];

        delegate.doFinal(digestBytes, 0);

        return digestBytes;
    }
}
