package com.jn.langx.security.gm.crypto.sm3;

import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;

public class SM3Jce extends BCMessageDigest
{
    public SM3Jce() {
        super(new SM3());
    }
}
