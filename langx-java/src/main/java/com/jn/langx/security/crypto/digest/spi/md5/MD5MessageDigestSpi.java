package com.jn.langx.security.crypto.digest.spi.md5;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._MD5Digest;

public class MD5MessageDigestSpi extends LangxMessageDigestSpi {
    public MD5MessageDigestSpi() {
        super(new _MD5Digest());
    }
}
