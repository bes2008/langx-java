package com.jn.langx.security.crypto.digest.spi.md4;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._MD4Digest;

public class MD4MessageDigest extends LangxMessageDigestSpi {
    public MD4MessageDigest(){
        super(new _MD4Digest());
    }
}
