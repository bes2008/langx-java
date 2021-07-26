package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class SHA256DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-256", Reflects.getFQNClassName(SHA256MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.SHA256", "SHA-256");
        provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.1", "SHA-256");
    }
}
