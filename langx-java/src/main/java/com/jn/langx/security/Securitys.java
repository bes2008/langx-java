package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;

import java.security.Provider;
import java.security.Security;

public class Securitys {

    public static void addProvider(Provider provider) {
        Security.addProvider(provider);
    }

    public static void setupLangxProvider() {
        addProvider(new LangxSecurityProvider());
    }

    public static boolean langxProviderInstalled() {
        return Security.getProvider(LangxSecurityProvider.NAME) != null;
    }
}
