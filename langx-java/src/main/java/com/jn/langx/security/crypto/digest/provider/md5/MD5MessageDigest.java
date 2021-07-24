package com.jn.langx.security.crypto.digest.provider.md5;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._MD5Digest;

public class MD5MessageDigest extends LangxMessageDigest {
    public MD5MessageDigest() {
        super(new _MD5Digest());
    }
}
