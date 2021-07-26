package com.jn.langx.security.crypto.digest.spi.sha224;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class SHA224DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-224", Reflects.getFQNClassName(SHA224MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.SHA224", "SHA-224");
        provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.4", "SHA-224");
    }
}
