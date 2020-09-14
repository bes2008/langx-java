package com.jn.langx.security.messagedigest.provider;

import com.jn.langx.security.messagedigest.DigestProviderConfigurer;
import com.jn.langx.security.messagedigest.LangxDigestProvider;

public class SM3DigestProviderConfigurer implements DigestProviderConfigurer {
    @Override
    public void configure(LangxDigestProvider provider) {
        provider.addAlgorithm("MessageDigest.SM3", SM3MessageDigest.class.getName());
        provider.addAlgorithm("Alg.Alias.MessageDigest.SM3", "SM3");
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");  // old draft OID - deprecated
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.10197.1.401", "SM3");
    }
}
