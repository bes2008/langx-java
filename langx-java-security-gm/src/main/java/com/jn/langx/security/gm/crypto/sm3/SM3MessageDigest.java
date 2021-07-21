package com.jn.langx.security.gm.crypto.sm3;

import com.jn.langx.security.gm.crypto.sm3.internal._SM3DigestImpl;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;

public class SM3MessageDigest extends BCMessageDigest {
    public SM3MessageDigest() {
        super(new _SM3DigestImpl());
    }
}
