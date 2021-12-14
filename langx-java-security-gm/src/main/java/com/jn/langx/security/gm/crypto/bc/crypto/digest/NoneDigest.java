package com.jn.langx.security.gm.crypto.bc.crypto.digest;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.util.Memoable;

import java.io.ByteArrayOutputStream;

public class NoneDigest extends GeneralDigest {
    private ByteArrayOutputStream bout;

    public NoneDigest() {
        this.bout = new ByteArrayOutputStream();
        this.reset();
    }

    @Override
    public void update(final byte b) {
        this.bout.write(b);
    }

    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.bout.write(array, n, n2);
    }

    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] byteArray = this.bout.toByteArray();
        if (byteArray.length == 32) {
            System.arraycopy(byteArray, 0, array, n, 32);
        }
        this.finish();
        this.reset();
        return byteArray.length;
    }

    @Override
    public String getAlgorithmName() {
        return "Dump";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public void reset() {
        super.reset();
        this.bout.reset();
    }

    @Override
    protected void processWord(final byte[] array, final int n) {
    }

    @Override
    protected void processLength(final long n) {
    }

    @Override
    protected void processBlock() {

    }

    public Memoable copy()
    {
        return new NoneDigest();
    }

    public void reset(Memoable other)
    {
        NoneDigest d = (NoneDigest)other;

        super.copyIn(d);
        copyIn(d);
    }


}
