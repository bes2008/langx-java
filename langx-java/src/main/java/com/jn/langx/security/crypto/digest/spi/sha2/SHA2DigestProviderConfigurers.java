package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;

public class SHA2DigestProviderConfigurers {

    public static class SHA224DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-224", SHA2MessageDigestSpis.SHA224MessageDigest.class);
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA224", "SHA-224");
            provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.4", "SHA-224");
        }
    }
    public static class SHA256DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-256", SHA2MessageDigestSpis.SHA256MessageDigest.class);
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA256", "SHA-256");
            provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.1", "SHA-256");
        }
    }
    public static class SHA384DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-384", SHA2MessageDigestSpis.SHA384MessageDigest.class);
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA384", "SHA-384");
            provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.2", "SHA-384");
        }
    }
    public static class SHA512DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
        @Override
        public void configure(LangxSecurityProvider provider) {
            provider.addAlgorithm("MessageDigest.SHA-512", SHA2MessageDigestSpis.SHA512MessageDigest.class);
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA512", "SHA-512");
            provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");

            provider.addAlgorithm("MessageDigest.SHA-512/224", SHA2MessageDigestSpis.SHA512T224MessageDigest.class);
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA512/224", "SHA-512/224");
            provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.5", "SHA-512/224");

            provider.addAlgorithm("MessageDigest.SHA-512/256", SHA2MessageDigestSpis.SHA512T256MessageDigest.class);
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA512256", "SHA-512/256");
            provider.addAlgorithm("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.6", "SHA-512/256");
        }
    }


}
