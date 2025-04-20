package com.jn.langx.security.crypto.pbe.pbkdf;

public class PBKDFEngineBuilder implements com.jn.langx.Builder<PBKDFEngine> {
    private DerivedKeyGeneratorFactory keyGeneratorFactory;
    private boolean useHMac = false;
    private boolean generateIV = false;

    public PBKDFEngineBuilder withKeyGeneratorFactory(DerivedKeyGeneratorFactory keyGeneratorFactory) {
        this.keyGeneratorFactory = keyGeneratorFactory;
        return this;
    }

    public PBKDFEngineBuilder withUseHMac(boolean useHMac) {
        this.useHMac = useHMac;
        return this;
    }

    public PBKDFEngineBuilder withGenerateIV(boolean generateIV) {
        this.generateIV = generateIV;
        return this;
    }

    @Override
    public PBKDFEngine build() {
        return new PBKDFEngine(keyGeneratorFactory, useHMac, generateIV);
    }
}
