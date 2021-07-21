package com.jn.langx.security.crypto.digest.provider.sm3;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.Xof;
import com.jn.langx.security.crypto.digest.internal.impl._SM3Digest;

public class SM3MessageDigest extends LangxMessageDigest {
    public SM3MessageDigest(){
        this(new _SM3Digest());
    }

    public SM3MessageDigest(_SM3Digest digest) {
        super(digest);
    }

    public SM3MessageDigest(Xof digest, int outputSize) {
        super(digest, outputSize);
    }
}
