package com.jn.langx.security.crypto.digest.provider.md2;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl._MD2Digest;

public class MD2MessageDigest extends LangxMessageDigest {
    public MD2MessageDigest(){
        super(new _MD2Digest());
    }
}
