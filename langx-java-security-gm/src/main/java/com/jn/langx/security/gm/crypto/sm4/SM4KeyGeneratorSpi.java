package com.jn.langx.security.gm.crypto.sm4;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;

public class SM4KeyGeneratorSpi extends BaseKeyGenerator {
    public SM4KeyGeneratorSpi() {
        super("SM4", 128, new CipherKeyGenerator());
    }
    public SM4KeyGeneratorSpi(String algName, int defaultKeySize, CipherKeyGenerator engine) {
        super(algName, defaultKeySize, engine);
    }
}
