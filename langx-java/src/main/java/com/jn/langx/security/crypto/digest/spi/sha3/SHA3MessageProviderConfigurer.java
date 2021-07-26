package com.jn.langx.security.crypto.digest.spi.sha3;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class SHA3MessageProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-3", SHA3MessageDigestSpis.SHA3MessageDigest.class);
        provider.addAlgorithm("MessageDigest.SHA3", SHA3MessageDigestSpis.SHA3MessageDigest.class);

        provider.addAlgorithm("MessageDigest.SHA3-224", SHA3MessageDigestSpis.SHA3_224MessageDigest.class);
        provider.addAlgorithm("MessageDigest.SHA3-256", SHA3MessageDigestSpis.SHA3_256MessageDigest.class);
        provider.addAlgorithm("MessageDigest.SHA3-384", SHA3MessageDigestSpis.SHA3_384MessageDigest.class);
        provider.addAlgorithm("MessageDigest.SHA3-512", SHA3MessageDigestSpis.SHA3_512MessageDigest.class);

        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.7", SHA3MessageDigestSpis.SHA3_224MessageDigest.class);
        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.8", SHA3MessageDigestSpis.SHA3_256MessageDigest.class);
        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.9", SHA3MessageDigestSpis.SHA3_384MessageDigest.class);
        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.10", SHA3MessageDigestSpis.SHA3_512MessageDigest.class);
    }
}
