package com.jn.langx.security.crypto.digest.spi.sm3;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class SM3DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SM3", SM3MessageDigestSpi.class);
        provider.addAlgorithm("Alg.Alias.MessageDigest.SM3", "SM3");
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");  // old draft OID - deprecated
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.10197.1.401", "SM3");

        provider.addHmacAlgorithm("SM3", HMacSM3Spi.class, HMacSM3KeyGeneratorSpi.class);
    }
}
