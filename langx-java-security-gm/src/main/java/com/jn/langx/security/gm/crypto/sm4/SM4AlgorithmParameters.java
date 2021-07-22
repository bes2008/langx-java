package com.jn.langx.security.gm.crypto.sm4;

import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;

public class SM4AlgorithmParameters extends IvAlgorithmParameters {
    protected String engineToString() {
        return "SM4 IV";
    }
}