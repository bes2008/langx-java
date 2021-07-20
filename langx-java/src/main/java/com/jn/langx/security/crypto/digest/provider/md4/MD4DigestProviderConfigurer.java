package com.jn.langx.security.crypto.digest.provider.md4;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.SecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public class MD4DigestProviderConfigurer implements SecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.MD4", Reflects.getFQNClassName(MD4MessageDigest.class));
        provider.addAlgorithm("Alg.Alias.MessageDigest." + PKCSObjectIdentifiers.md4, "MD4");
    }
}
