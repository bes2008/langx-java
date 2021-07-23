package com.jn.langx.security.gm.crypto;

import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;

public class SM2CbcBlockCipher extends BaseBlockCipher {
    public SM2CbcBlockCipher() {
        super(new CBCBlockCipher(new SM2BouncyCastleBlockCipher()));
    }
}
