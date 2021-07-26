package com.jn.langx.security.crypto.digest.spi.whirlpool;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._WhirlpoolDigest;

public class WhirlpoolMessageDigestSpi extends LangxMessageDigestSpi {
    public WhirlpoolMessageDigestSpi(){
        super(new _WhirlpoolDigest());
    }
}
