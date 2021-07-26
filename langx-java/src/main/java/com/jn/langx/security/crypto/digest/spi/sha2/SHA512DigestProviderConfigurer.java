package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class SHA512DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-512", Reflects.getFQNClassName(SHA512MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.SHA512", "SHA-512");
        provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");

        provider.addAlgorithm("MessageDigest.SHA-512/224", Reflects.getFQNClassName(SHA512T224MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.SHA512/224", "SHA-512/224");
        provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.5", "SHA-512/224");

        provider.addAlgorithm("MessageDigest.SHA-512/256", Reflects.getFQNClassName(SHA512T256MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.SHA512256", "SHA-512/256");
        provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.6", "SHA-512/256");
    }
}
