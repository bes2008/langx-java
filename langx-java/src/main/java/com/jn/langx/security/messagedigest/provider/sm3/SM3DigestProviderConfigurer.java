package com.jn.langx.security.messagedigest.provider.sm3;

import com.jn.langx.security.provider.SecurityProviderConfigurer;
import com.jn.langx.security.provider.LangxSecurityProvider;

public class SM3DigestProviderConfigurer implements SecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SM3", SM3MessageDigest.class.getName());
        provider.addAlgorithm("Alg.Alias.MessageDigest.SM3", "SM3");
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");  // old draft OID - deprecated
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.10197.1.401", "SM3");
    }
}
