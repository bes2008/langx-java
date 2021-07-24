package com.jn.langx.security.crypto.digest.provider.md5;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class MD5DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.MD5", Reflects.getFQNClassName(MD5MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.840.113549.2.5", "MD5");
    }
}
