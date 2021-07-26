package com.jn.langx.security.crypto.digest.spi.sm3;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._SM3Digest;

public class SM3MessageDigestSpi extends LangxMessageDigestSpi {
    public SM3MessageDigestSpi(){
        super(new _SM3Digest());
    }
}
