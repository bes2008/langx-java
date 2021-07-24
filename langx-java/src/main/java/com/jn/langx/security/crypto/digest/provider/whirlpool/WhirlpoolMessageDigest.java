package com.jn.langx.security.crypto.digest.provider.whirlpool;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._WhirlpoolDigest;

public class WhirlpoolMessageDigest extends LangxMessageDigest {
    public WhirlpoolMessageDigest(){
        super(new _WhirlpoolDigest());
    }
}
