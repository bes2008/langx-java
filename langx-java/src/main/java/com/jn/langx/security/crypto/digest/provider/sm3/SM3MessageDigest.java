package com.jn.langx.security.crypto.digest.provider.sm3;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._SM3Digest;

public class SM3MessageDigest extends LangxMessageDigest {
    public SM3MessageDigest(){
        super(new _SM3Digest());
    }
}
