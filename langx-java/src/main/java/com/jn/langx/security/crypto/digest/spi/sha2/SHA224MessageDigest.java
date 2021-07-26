package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._SHA224Digest;

public class SHA224MessageDigest extends LangxMessageDigest {
    public SHA224MessageDigest(){
        super(new _SHA224Digest());
    }
}
