package com.jn.langx.security.crypto.digest.provider.sha256;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._SHA256Digest;

public class SHA256MessageDigest extends LangxMessageDigest {
    public SHA256MessageDigest(){
        super(new _SHA256Digest());
    }
}
