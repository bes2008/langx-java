package com.jn.langx.security.pbe.cipher.kdf;


import com.jn.langx.security.Securitys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public abstract class PBKDFKeyGeneratorSpi extends KeyGeneratorSpi {
    protected SecureRandom secureRandom;
    protected PBKDFAlgorithmParameterSpec algorithmParameter;

    @Override
    protected void engineInit(SecureRandom secureRandom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if(Reflects.isSubClassOrEquals(PBKDFAlgorithmParameterSpec.class, algorithmParameterSpec.getClass())){
            this.algorithmParameter=(PBKDFAlgorithmParameterSpec) algorithmParameter;
        }else{
            throw new InvalidAlgorithmParameterException("parameter invalid");
        }
        this.secureRandom= Objs.useValueIfEmpty(secureRandom, Securitys.getSecureRandom());
    }

    @Override
    protected void engineInit(int keyBitSize, SecureRandom secureRandom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SecretKey engineGenerateKey() {
        return engineGenerateDerivedKey();
    }

    protected abstract DerivedKey engineGenerateDerivedKey();
}
