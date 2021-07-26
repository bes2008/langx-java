package com.jn.langx.security.crypto.digest.spi.sha512;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._SHA512tDigest;

public class SHA512TMessageDigest extends LangxMessageDigest {
    public SHA512TMessageDigest(int bitLength) {
        super(new _SHA512tDigest(bitLength));
    }
}
