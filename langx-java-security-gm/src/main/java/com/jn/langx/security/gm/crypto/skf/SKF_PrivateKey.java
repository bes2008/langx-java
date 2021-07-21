package com.jn.langx.security.gm.crypto.skf;


import java.security.InvalidKeyException;
import java.security.PrivateKey;

public class SKF_PrivateKey implements PrivateKey
{
    private static final long serialVersionUID = 4165752956339128733L;
    private ICryptoProvider cryptoProvider;
    private int sig;

    public SKF_PrivateKey(final ICryptoProvider cryptoProvider, final int sig) {
        this.cryptoProvider = null;
        this.sig = 0;
        this.cryptoProvider = cryptoProvider;
        this.sig = sig;
    }

    @Override
    public String getAlgorithm() {
        return "SM2";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return null;
    }

    public int getKeyLength() throws InvalidKeyException {
        return 32;
    }

    public ICryptoProvider getCryptoProvider() {
        return this.cryptoProvider;
    }

    @Override
    public String toString() {
        return "SM2 Pri(sig=" + this.sig + ")";
    }
}
