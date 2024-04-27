package com.jn.langx.security.crypto.key;

import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKeyFactorySpi;
import java.security.Provider;

public class LangxSecretKeyFactory extends SecretKeyFactory {
    public LangxSecretKeyFactory(SecretKeyFactorySpi keyFacSpi, Provider provider, String algorithm) {
      super(keyFacSpi, provider, algorithm);
    }
}
