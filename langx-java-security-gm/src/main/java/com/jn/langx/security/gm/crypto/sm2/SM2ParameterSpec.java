package com.jn.langx.security.gm.crypto.sm2;


import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

public class SM2ParameterSpec implements AlgorithmParameterSpec {
    private byte[] id;
    private PublicKey publicKey;

    public SM2ParameterSpec(final byte[] id, final PublicKey publicKey) {
        this.id = null;
        this.publicKey = null;
        this.publicKey = publicKey;
        this.id = id;
    }

    public byte[] getId() {
        return this.id;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
