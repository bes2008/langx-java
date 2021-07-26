package com.jn.langx.security.crypto.digest.spi.md2;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._MD2Digest;

public class MD2MessageDigestSpi extends LangxMessageDigestSpi {
    public MD2MessageDigestSpi(){
        super(new _MD2Digest());
    }
}
