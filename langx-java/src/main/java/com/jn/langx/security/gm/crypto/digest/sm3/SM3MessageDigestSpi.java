package com.jn.langx.security.gm.crypto.digest.sm3;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;

public class SM3MessageDigestSpi extends LangxMessageDigestSpi {
    public SM3MessageDigestSpi(){
        super(new _SM3Digest());
    }
}
