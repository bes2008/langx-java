package com.jn.langx.security.gm.crypto.sm2;


import java.security.spec.*;
import java.security.*;
import java.math.*;

/**
 * @ses cn.gmssl.crypto.SM2KeyExchangeParams
 */
public class SM2KeyExchangeParameterSpec implements AlgorithmParameterSpec
{
    private PublicKey publicKey;
    private PublicKey peerPublicKey;
    private BigInteger random;
    private byte[] idLocal;
    private byte[] idRemote;
    private int keyLength;
    boolean active;

    public SM2KeyExchangeParameterSpec(final PublicKey publicKey, final PublicKey peerPublicKey, final BigInteger random, final byte[] idLocal, final byte[] idRemote, final int keyLength, final boolean active) {
        this.publicKey = null;
        this.peerPublicKey = null;
        this.random = null;
        this.idLocal = null;
        this.idRemote = null;
        this.keyLength = 0;
        this.active = false;
        this.publicKey = publicKey;
        this.peerPublicKey = peerPublicKey;
        this.random = random;
        this.idLocal = idLocal;
        this.idRemote = idRemote;
        this.active = active;
        this.keyLength = keyLength;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PublicKey getPeerPublicKey() {
        return this.peerPublicKey;
    }

    public BigInteger getRandom() {
        return this.random;
    }

    public boolean isActive() {
        return this.active;
    }

    public byte[] getIdLocal() {
        return this.idLocal;
    }

    public byte[] getIdRemote() {
        return this.idRemote;
    }

    public int getKeyLength() {
        return this.keyLength;
    }
}
