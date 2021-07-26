package com.jn.langx.security.crypto.digest.spi.md5;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class MD5DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.MD5", MD5MessageDigestSpi.class);
        provider.addAlias("MessageDigest.1.2.840.113549.2.5", "MD5");

        provider.addHmacAlgorithm("MD5", HMacMD5Spi.class, HMacMD5KeyGeneratorSpi.class);
        provider.addHmacOidAlias("1.3.6.1.5.5.8.1.1", "MD5");
    }
}
