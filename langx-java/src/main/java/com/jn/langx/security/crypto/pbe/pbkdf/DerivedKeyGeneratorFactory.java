package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.Factory;

public interface DerivedKeyGeneratorFactory<G extends DerivedKeyGenerator> extends Factory<PBKDFKeySpec, G> {
    @Override
    public G get(PBKDFKeySpec keySpec);
}
