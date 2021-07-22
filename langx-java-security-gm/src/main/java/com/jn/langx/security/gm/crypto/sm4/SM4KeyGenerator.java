package com.jn.langx.security.gm.crypto.sm4;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;

public class SM4KeyGenerator extends BaseKeyGenerator {
    public SM4KeyGenerator() {
        super("SM4", 128, new CipherKeyGenerator());
    }
    public SM4KeyGenerator(String algName, int defaultKeySize, CipherKeyGenerator engine) {
        super(algName, defaultKeySize, engine);
    }
}
