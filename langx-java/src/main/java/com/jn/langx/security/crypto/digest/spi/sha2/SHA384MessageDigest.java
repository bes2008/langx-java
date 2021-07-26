package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._SHA384Digest;

public class SHA384MessageDigest extends LangxMessageDigest {
    public SHA384MessageDigest(){
        super(new _SHA384Digest());
    }
}
