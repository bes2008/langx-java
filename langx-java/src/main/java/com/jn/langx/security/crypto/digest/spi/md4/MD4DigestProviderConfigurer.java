package com.jn.langx.security.crypto.digest.spi.md4;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class MD4DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.MD4", MD4MessageDigestSpi.class);
        provider.addAlgorithmOid("Alg.Alias.MessageDigest", "1.2.840.113549.2.4", "MD4");
    }
}
