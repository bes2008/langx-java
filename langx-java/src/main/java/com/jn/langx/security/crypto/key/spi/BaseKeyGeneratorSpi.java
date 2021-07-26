package com.jn.langx.security.crypto.key.spi;


import com.jn.langx.security.crypto.key.SecureRandoms;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class BaseKeyGeneratorSpi extends KeyGeneratorSpi {
    protected String algName;
    /**
     * unit in bytes
     */
    protected int keySize;

    /**
     * unit in bytes
     */
    protected int defaultKeySize;
    protected CipherKeyGeneratorEngine engine;

    protected boolean initialed = false;

    public BaseKeyGeneratorSpi(String algorithm, int defaultKeySize) {
        this(algorithm, defaultKeySize, new CipherKeyGeneratorEngine());
    }

    public BaseKeyGeneratorSpi(String algName, int defaultKeySize, CipherKeyGeneratorEngine engine) {
        this.algName = algName;
        this.keySize = this.defaultKeySize = defaultKeySize;
        this.engine = engine;
    }

    protected void engineInit(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Not Implemented");
    }

    protected void engineInit(SecureRandom random) {
        engineInit(defaultKeySize, random);
    }

    protected void engineInit(int keySize, SecureRandom random) {
        try {
            if (random == null) {
                random = SecureRandoms.getDefault();
            }
            engine.init(keySize, random);
            initialed = true;
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    protected SecretKey engineGenerateKey() {
        if (!initialed) {
            engineInit(defaultKeySize, SecureRandoms.getDefault());
        }

        return new SecretKeySpec(engine.generateKey(), algName);
    }
}
