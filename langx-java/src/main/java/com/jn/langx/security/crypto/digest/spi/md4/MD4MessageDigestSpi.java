package com.jn.langx.security.crypto.digest.spi.md4;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._MD4Digest;

public class MD4MessageDigestSpi extends LangxMessageDigestSpi {
    public MD4MessageDigestSpi(){
        super(new _MD4Digest());
    }
}
