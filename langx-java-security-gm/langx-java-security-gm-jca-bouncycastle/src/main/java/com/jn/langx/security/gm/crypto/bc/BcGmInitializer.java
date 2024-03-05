package com.jn.langx.security.gm.crypto.bc;

import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.*;
import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
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
        Map<String, String> absentMap = new HashMap<String, String>();
        Map<String, String> overwriteMap = new HashMap<String, String>();

        // sm2
        absentMap.put("Signature.SM3WithSM2", Reflects.getFQNClassName(SM2SignatureSpi.SM3WithSM2.class));
        absentMap.put("Signature.1.2.156.10197.1.501", Reflects.getFQNClassName(SM2SignatureSpi.SM3WithSM2.class));
        absentMap.put("AlgorithmParameters.SM2", Reflects.getFQNClassName(SM2AlgorithmParametersSpi.class));
        absentMap.put("KeyFactory.SM2", Reflects.getFQNClassName(SM2KeyFactorySpi.class));
        absentMap.put("KeyPairGenerator.SM2", Reflects.getFQNClassName(SM2KeyPairGeneratorSpi.class));

        boolean sm2DefaultC1C3C2ModeEnabled= GMs.sm2DefaultC1C3C2ModeEnabled();
        if(sm2DefaultC1C3C2ModeEnabled){
            overwriteMap.put("Cipher.SM2", Reflects.getFQNClassName(SM2xCipherSpi.SM2withSm3.class));
            overwriteMap.put("Alg.Alias.Cipher.SM2WITHSM3", "SM2");
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_sm3, "SM2");
            overwriteMap.put("Cipher.SM2WITHBLAKE2B", Reflects.getFQNClassName(SM2xCipherSpi.SM2withBlake2b.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_blake2b512, "SM2WITHBLAKE2B");
            overwriteMap.put("Cipher.SM2WITHBLAKE2S", Reflects.getFQNClassName(SM2xCipherSpi.SM2withBlake2s.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_blake2s256, "SM2WITHBLAKE2S");
            overwriteMap.put("Cipher.SM2WITHWHIRLPOOL", Reflects.getFQNClassName(SM2xCipherSpi.SM2withWhirlpool.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_whirlpool, "SM2WITHWHIRLPOOL");
            overwriteMap.put("Cipher.SM2WITHMD5", Reflects.getFQNClassName(SM2xCipherSpi.SM2withMD5.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_md5, "SM2WITHMD5");
            overwriteMap.put("Cipher.SM2WITHRIPEMD160", Reflects.getFQNClassName(SM2xCipherSpi.SM2withRMD.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_rmd160, "SM2WITHRIPEMD160");
            overwriteMap.put("Cipher.SM2WITHSHA1", Reflects.getFQNClassName(SM2xCipherSpi.SM2withSha1.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_sha1, "SM2WITHSHA1");
            overwriteMap.put("Cipher.SM2WITHSHA224", Reflects.getFQNClassName(SM2xCipherSpi.SM2withSha224.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_sha224, "SM2WITHSHA224");
            overwriteMap.put("Cipher.SM2WITHSHA256", Reflects.getFQNClassName(SM2xCipherSpi.SM2withSha256.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_sha256, "SM2WITHSHA256");
            overwriteMap.put("Cipher.SM2WITHSHA384", Reflects.getFQNClassName(SM2xCipherSpi.SM2withSha384.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_sha384, "SM2WITHSHA384");
            overwriteMap.put("Cipher.SM2WITHSHA512", Reflects.getFQNClassName(SM2xCipherSpi.SM2withSha512.class));
            overwriteMap.put("Alg.Alias.Cipher." + GMObjectIdentifiers.sm2encrypt_with_sha512, "SM2WITHSHA512");
        }
        // sm3
        absentMap.put("MessageDigest.SM3", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.digest.SM3.Digest.class));
        absentMap.put("Alg.Alias.MessageDigest.SM3", "SM3");
        absentMap.put("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");

        // sm4
        // 配置 SM4 算法： org.bouncycastle.jcajce.provider.symmetric.SM4.Mappings
        absentMap.put("AlgorithmParameters.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.AlgParams.class));
        absentMap.put("AlgorithmParameterGenerator.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.AlgParamGen.class));
        absentMap.put("Cipher.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.ECB.class));
        absentMap.put("KeyGenerator.SM4", Reflects.getFQNClassName(org.bouncycastle.jcajce.provider.symmetric.SM4.KeyGen.class));

        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            provider = new BouncyCastleProvider();
            Securitys.addProvider(provider);
        }
        final Provider _provider = provider;
        Collects.forEach(absentMap, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                if (!_provider.containsKey(key)) {
                    _provider.put(key, value);
                }
            }
        });

        Collects.forEach(overwriteMap, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                _provider.put(key, value);
            }
        });
    }

}