package com.jn.langx.security.crypto.digest.spi.sha3;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class SHA3MessageProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-3", SHA3MessageDigestSpis.SHA3MessageDigest.class);
        provider.addAlgorithm("MessageDigest.SHA3", SHA3MessageDigestSpis.SHA3MessageDigest.class);

        provider.addAlgorithm("MessageDigest.SHA3-224", SHA3MessageDigestSpis.SHA3_224MessageDigestSpi.class);
        provider.addAlgorithm("MessageDigest.SHA3-256", SHA3MessageDigestSpis.SHA3_256MessageDigestSpi.class);
        provider.addAlgorithm("MessageDigest.SHA3-384", SHA3MessageDigestSpis.SHA3_384MessageDigestSpi.class);
        provider.addAlgorithm("MessageDigest.SHA3-512", SHA3MessageDigestSpis.SHA3_512MessageDigestSpi.class);

        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.7", SHA3MessageDigestSpis.SHA3_224MessageDigestSpi.class);
        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.8", SHA3MessageDigestSpis.SHA3_256MessageDigestSpi.class);
        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.9", SHA3MessageDigestSpis.SHA3_384MessageDigestSpi.class);
        provider.addAlgorithmOid("MessageDigest", "2.16.840.1.101.3.4.2.10", SHA3MessageDigestSpis.SHA3_512MessageDigestSpi.class);


        provider.addHmacAlgorithm("SHA3-224", SHA3MessageDigestSpis.HMacSHA3_224Spi.class, SHA3MessageDigestSpis.HMacSHA3_224KeyGeneratorSpi.class);
        provider.addHmacOidAlias("2.16.840.1.101.3.4.2.13","SHA3-224");
        provider.addHmacAlgorithm("SHA3-256", SHA3MessageDigestSpis.HMacSHA3_256Spi.class, SHA3MessageDigestSpis.HMacSHA3_256KeyGeneratorSpi.class);
        provider.addHmacOidAlias("2.16.840.1.101.3.4.2.14","SHA3-256");
        provider.addHmacAlgorithm("SHA3-384", SHA3MessageDigestSpis.HMacSHA3_384Spi.class, SHA3MessageDigestSpis.HMacSHA3_384KeyGeneratorSpi.class);
        provider.addHmacOidAlias("2.16.840.1.101.3.4.2.15","SHA3-384");
        provider.addHmacAlgorithm("SHA3-512", SHA3MessageDigestSpis.HMacSHA3_512Spi.class, SHA3MessageDigestSpis.HMacSHA3_512KeyGeneratorSpi.class);
        provider.addHmacOidAlias("2.16.840.1.101.3.4.2.16","SHA3-512");
    }
}
