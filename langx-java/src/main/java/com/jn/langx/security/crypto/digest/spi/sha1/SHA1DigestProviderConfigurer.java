package com.jn.langx.security.crypto.digest.spi.sha1;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class SHA1DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-1", SHA1MessageDigestSpi.class);
        provider.addAlias("MessageDigest.SHA1", "SHA-1");
        provider.addAlias("MessageDigest.SHA", "SHA-1");
        provider.addAlias("MessageDigest.1.3.14.3.2.26", "SHA-1");

        provider.addHmacAlgorithm("SHA1", HMacSHA1Spi.class, HMacSHA1KeyGeneratorSpi.class);
        provider.addHmacOidAlias("1.2.840.113549.2.7", "SHA1");
        provider.addHmacOidAlias("1.3.6.1.5.5.8.1.2", "SHA1");
    }
}
