package com.jn.langx.security.gm.crypto;

import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;

public class SM2EcbBlockCipher extends BaseBlockCipher {
    public SM2EcbBlockCipher() {
        super(new SM2BouncyCastleBlockCipher());
    }
}
