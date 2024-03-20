package com.jn.langx.security.gm.crypto.bc.asymmetric.sm2;

import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;

/**
 * SM2 用的就是 EC的 key pair
 */
public class SM2KeyPairGeneratorSpi extends KeyPairGeneratorSpi.EC {
    private KeyPairGenerator ecKeyPairGenerator;
    public SM2KeyPairGeneratorSpi(){
        super();
        try {
            this.ecKeyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        }catch (Throwable e){
            // ignore it
        }
    }

    @Override
    public void initialize(int strength, SecureRandom random) {
        init();
    }

    @Override
    public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        init();
    }

    @Override
    public void initialize(int keysize) {
        init();
    }

    @Override
    public void initialize(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
        init();
    }

    private void init(){
        try {
            this.ecKeyPairGenerator.initialize(new ECGenParameterSpec("sm2p256v1"));
        }catch (Exception e){
            // ignore it
        }
    }

    @Override
    public KeyPair generateKeyPair() {
        return this.ecKeyPairGenerator.generateKeyPair();
    }
}
