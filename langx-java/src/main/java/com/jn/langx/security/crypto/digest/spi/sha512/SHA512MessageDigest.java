package com.jn.langx.security.crypto.digest.spi.sha512;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._SHA512Digest;

public class SHA512MessageDigest extends LangxMessageDigest {
    public SHA512MessageDigest(){
        super(new _SHA512Digest());
    }
}
