package com.jn.langx.security.crypto.digest.spi.whirlpool;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl._WhirlpoolDigest;

public class WhirlpoolMessageDigest extends LangxMessageDigestSpi {
    public WhirlpoolMessageDigest(){
        super(new _WhirlpoolDigest());
    }
}
