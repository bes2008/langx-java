package com.jn.langx.security.gm.crypto.bc.asymmetric.sm2;

import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;

import java.security.spec.ECGenParameterSpec;

/**
 * SM2 用的就是 EC的 key pair
 */
public class SM2KeyPairGeneratorSpi extends KeyPairGeneratorSpi.EC {

    SM2KeyPairGeneratorSpi(){
        super();
        try {
            initialize(new ECGenParameterSpec("sm2p256v1"));
        }catch (Exception e){
            // ignore it
        }
    }
}
