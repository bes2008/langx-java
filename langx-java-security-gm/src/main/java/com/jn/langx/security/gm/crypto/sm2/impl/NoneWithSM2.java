package com.jn.langx.security.gm.crypto.sm2.impl;

public class NoneWithSM2 extends BCSignatureSpi {
    public NoneWithSM2() {
        super(new NoneDigest(), new SM2Signer(), new StdDSAEncoder());
    }
}
