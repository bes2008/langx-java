package com.jn.langx.security.crypto.digest.spi.md2;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class MD2DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.MD2", MD2MessageDigestSpi.class);
        provider.addAlias("MessageDigest.1.2.840.113549.2.2", "MD2");

        provider.addHmacAlgorithm("MD2", HmacMD2Spi.class, HmacMD2KeyGeneratorSpi.class);
    }
}
