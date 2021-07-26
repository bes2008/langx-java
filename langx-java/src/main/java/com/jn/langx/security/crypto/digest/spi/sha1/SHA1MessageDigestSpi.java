package com.jn.langx.security.crypto.digest.spi.sha1;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._SHA1Digest;

public class SHA1MessageDigestSpi extends LangxMessageDigestSpi {
    public SHA1MessageDigestSpi(){
        super(new _SHA1Digest());
    }
}
