package com.jn.langx.security.crypto.digest.provider.sha384;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class SHA384DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-384", Reflects.getFQNClassName(SHA384MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.SHA384", "SHA-384");
        provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.2", "SHA-384");
    }
}
