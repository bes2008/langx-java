package com.jn.langx.security.crypto.pbe.pbkdf;


public class PBKDFEngine implements PBKDF {
    private DerivedKeyGeneratorFactory keyGeneratorFactory;
    private boolean useHMac;
    private boolean generateIV;


    public PBKDFEngine(DerivedKeyGeneratorFactory keyGeneratorFactory, boolean useHMac, boolean generateIV) {
        this.keyGeneratorFactory = keyGeneratorFactory;
        this.useHMac = useHMac;
        this.generateIV = generateIV;
    }

    @Override
    public DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec) {
        SimpleDerivedKey simpleDerivedKey = generateDerivedKey(keySpec);
        DerivedPBEKey derivedPBEKey = new DerivedPBEKey(pbeAlgorithm, keySpec, simpleDerivedKey.getDerivedKey(), simpleDerivedKey.getIv());
        return derivedPBEKey;
    }

    protected SimpleDerivedKey generateDerivedKey(PBKDFKeySpec keySpec) {
        DerivedKeyGenerator keyGenerator = keyGeneratorFactory.get(keySpec);
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
