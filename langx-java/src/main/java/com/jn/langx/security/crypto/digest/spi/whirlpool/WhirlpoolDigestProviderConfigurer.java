package com.jn.langx.security.crypto.digest.spi.whirlpool;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class WhirlpoolDigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.WHIRLPOOL", Reflects.getFQNClassName(WhirlpoolMessageDigestSpi.class));
        provider.addAlgorithmOid("MessageDigest", "1.0.10118.3.0.55", Reflects.getFQNClassName(WhirlpoolMessageDigestSpi.class));
    }
}
