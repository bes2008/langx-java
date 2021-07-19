package com.jn.langx.security.crypto.messagedigest.provider.sm3;

import com.jn.langx.security.crypto.messagedigest.LangxMessageDigest;
import com.jn.langx.security.crypto.messagedigest.digest.Xof;
import com.jn.langx.security.crypto.messagedigest.digest.impl.sm3.SM3Digest;

public class SM3MessageDigest extends LangxMessageDigest {
    public SM3MessageDigest(){
        this(new SM3Digest());
    }

    public SM3MessageDigest(SM3Digest digest) {
        super(digest);
    }

    public SM3MessageDigest(Xof digest, int outputSize) {
        super(digest, outputSize);
    }
}
