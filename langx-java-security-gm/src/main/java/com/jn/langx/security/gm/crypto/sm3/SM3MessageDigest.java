package com.jn.langx.security.gm.crypto.sm3;

import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;

public class SM3MessageDigest extends BCMessageDigest {
    public SM3MessageDigest() {
        super(new SM3DigestImpl());
    }
}
