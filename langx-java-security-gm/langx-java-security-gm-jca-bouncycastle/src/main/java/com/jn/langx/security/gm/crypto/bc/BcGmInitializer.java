package com.jn.langx.security.gm.crypto.bc;

import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2AlgorithmParametersSpi;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2CipherSpi;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2KeyFactorySpi;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2SignatureSpi;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.Serializable;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class BcGmInitializer extends AbstractInitializable implements GmInitializer, Serializable {
    private static final long serialVersionUID = 1443132961964116159L;

    @Override
    protected void doInit() throws InitializationException {
       initProvider();

    }

    private void initProvider(){
        Map<String, String> map = new HashMap<String, String>();

        // sm2
        map.put("Signature.SM3WithSM2", Reflects.getFQNClassName(SM2SignatureSpi.SM3WithSM2.class));
        map.put("Signature.1.2.156.10197.1.501", Reflects.getFQNClassName(SM2SignatureSpi.SM3WithSM2.class));
        map.put("Cipher.SM2", Reflects.getFQNClassName(SM2CipherSpi.class));
        map.put("AlgorithmParameters.SM2", Reflects.getFQNClassName(SM2AlgorithmParametersSpi.class));
        map.put("KeyFactory.SM2", Reflects.getFQNClassName(SM2KeyFactorySpi.class));
        map.put("KeyFactory.1.2.840.10045.2.1", Reflects.getFQNClassName(SM2KeyFactorySpi.class));
        map.put("KeyPairGenerator.SM2", Reflects.getFQNClassName(KeyPairGeneratorSpi.EC.class));

        // sm3
        map.put("MessageDigest.SM3", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.digest.SM3.Digest.class));
        map.put("Alg.Alias.MessageDigest.SM3", "SM3");
        map.put("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");

        // sm4
        map.put("AlgorithmParameters.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.AlgParams.class));
        map.put("AlgorithmParameterGenerator.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.AlgParamGen.class));
        map.put("Cipher.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.ECB.class));
        map.put("KeyGenerator.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.KeyGen.class));

        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            provider = new BouncyCastleProvider();
            Securitys.addProvider(provider);
        }
        final Provider p = provider;
        Collects.forEach(map, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                if (!p.containsKey(key)) {
                    p.put(key, value);
                }
            }
        });
    }

}