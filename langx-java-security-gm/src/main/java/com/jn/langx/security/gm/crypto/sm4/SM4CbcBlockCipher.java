package com.jn.langx.security.gm.crypto.sm4;

import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;

/**
 * @see // cn.gmssl.crypto.impl.SM4JCE$CBC
 */
public class SM4CbcBlockCipher extends BaseBlockCipher {
    public SM4CbcBlockCipher() {
        super(new CBCBlockCipher(new SM4BlockCipher()), 128);
    }
}
