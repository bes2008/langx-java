package com.jn.langx.security.crypto.digest.provider.md4;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.takeself.Digest;
import com.jn.langx.security.crypto.digest.takeself.Xof;
import com.jn.langx.security.crypto.digest.takeself.impl.MD4Digest;

public class MD4MessageDigest extends LangxMessageDigest {
    public MD4MessageDigest(){
        this(new MD4Digest());
    }
    public MD4MessageDigest(Digest digest) {
        super(digest);
    }

    public MD4MessageDigest(Xof digest, int outputSize) {
        super(digest, outputSize);
    }
}
