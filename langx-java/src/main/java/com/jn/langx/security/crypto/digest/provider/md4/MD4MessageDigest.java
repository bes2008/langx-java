package com.jn.langx.security.crypto.digest.provider.md4;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.Digest;
import com.jn.langx.security.crypto.digest.internal.Xof;
import com.jn.langx.security.crypto.digest.internal.impl._MD4Digest;

public class MD4MessageDigest extends LangxMessageDigest {
    public MD4MessageDigest(){
        this(new _MD4Digest());
    }
    public MD4MessageDigest(Digest digest) {
        super(digest);
    }

    public MD4MessageDigest(Xof digest, int outputSize) {
        super(digest, outputSize);
    }
}
