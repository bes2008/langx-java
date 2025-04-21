package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.util.Strings;

public class PBKDFEngine implements PBKDF {
    private DerivedKeyGeneratorFactory keyGeneratorFactory;


    public PBKDFEngine(DerivedKeyGeneratorFactory keyGeneratorFactory) {
        this.keyGeneratorFactory = keyGeneratorFactory;
    }

    @Override
    public DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec) {
        SimpleDerivedKey simpleDerivedKey = generateDerivedKey(keySpec);
        DerivedPBEKey derivedPBEKey = new DerivedPBEKey(pbeAlgorithm, keySpec, simpleDerivedKey.getDerivedKey(), simpleDerivedKey.getIv());
        return derivedPBEKey;
    }

    protected SimpleDerivedKey generateDerivedKey(PBKDFKeySpec keySpec) {
        DerivedKeyGenerator keyGenerator = keyGeneratorFactory.get(keySpec);
        boolean useHMac = Strings.startsWith(keySpec.getHashAlgorithm(), "Hmac", true);
        boolean generateIV = keySpec.getIvBitSize() >= 8 * 8; // 8 byte
        SimpleDerivedKey simpleDerivedKey = null;
        if (generateIV) {
            simpleDerivedKey = keyGenerator.generateDerivedKeyWithIV(keySpec.getKeyLength(), keySpec.getIvBitSize());
        } else if (useHMac) {
            simpleDerivedKey = keyGenerator.generateDerivedKeyUseHMac(keySpec.getKeyLength());
        } else {
            simpleDerivedKey = keyGenerator.generateDerivedKey(keySpec.getKeyLength());
        }
        return simpleDerivedKey;
    }

}
