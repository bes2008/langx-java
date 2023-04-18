package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class SHA2DigestProviderConfigurers {
    private SHA2DigestProviderConfigurers(){

    }
    public static class SHA224DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-224", SHA2MessageDigestSpis.SHA224MessageDigest.class);
            provider.addAlias("MessageDigest.SHA224", "SHA-224");
            provider.addAlias("MessageDigest.2.16.840.1.101.3.4.2.4", "SHA-224");

            provider.addHmacAlgorithm("SHA224", HMacSHA2Spis.HMacSHA224Spi.class, HMacSHA2Spis.HMacSHA224KeyGeneratorSpi.class);
            provider.addHmacOidAlias("1.2.840.113549.2.8", "SHA224");
        }
    }

    public static class SHA256DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-256", SHA2MessageDigestSpis.SHA256MessageDigest.class);
            provider.addAlias("MessageDigest.SHA256", "SHA-256");
            provider.addAlias("MessageDigest.2.16.840.1.101.3.4.2.1", "SHA-256");

            provider.addHmacAlgorithm("SHA256", HMacSHA2Spis.HMacSHA256Spi.class, HMacSHA2Spis.HMacSHA256KeyGeneratorSpi.class);
            provider.addHmacOidAlias("1.2.840.113549.2.9", "SHA256");
            provider.addHmacOidAlias("2.16.840.1.101.3.4.2.1", "SHA256");
        }
    }

    public static class SHA384DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-384", SHA2MessageDigestSpis.SHA384MessageDigest.class);
            provider.addAlias("MessageDigest.SHA384", "SHA-384");
            provider.addAlias("MessageDigest.2.16.840.1.101.3.4.2.2", "SHA-384");

            provider.addHmacAlgorithm("SHA384", HMacSHA2Spis.HMacSHA384Spi.class, HMacSHA2Spis.HMacSHA384KeyGeneratorSpi.class);
            provider.addHmacOidAlias("1.2.840.113549.2.10", "SHA384");
        }
    }

    public static class SHA512DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-512", SHA2MessageDigestSpis.SHA512MessageDigest.class);
            provider.addAlias("MessageDigest.SHA512", "SHA-512");
            provider.addAlias("MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");

            provider.addHmacAlgorithm("SHA512", HMacSHA2Spis.HMacSHA512Spi.class, HMacSHA2Spis.HMacSHA512KeyGeneratorSpi.class);
            provider.addHmacOidAlias("1.2.840.113549.2.11", "SHA512");

            // ----
            provider.addAlgorithm("MessageDigest.SHA-512/224", SHA2MessageDigestSpis.SHA512T224MessageDigest.class);
            provider.addAlias("MessageDigest.SHA512/224", "SHA-512/224");
            provider.addAlias("MessageDigest.2.16.840.1.101.3.4.2.5", "SHA-512/224");
            provider.addHmacAlgorithm("SHA512/224", HMacSHA2Spis.HMacSHA512T224Spi.class, HMacSHA2Spis.HMacSHA512T224KeyGeneratorSpi.class);

            provider.addAlgorithm("MessageDigest.SHA-512/256", SHA2MessageDigestSpis.SHA512T256MessageDigest.class);
            provider.addAlias("MessageDigest.SHA512256", "SHA-512/256");
            provider.addAlias("MessageDigest.2.16.840.1.101.3.4.2.6", "SHA-512/256");
            provider.addHmacAlgorithm("SHA512/256", HMacSHA2Spis.HMacSHA512T256Spi.class, HMacSHA2Spis.HMacSHA512T256KeyGeneratorSpi.class);
        }
    }


}
