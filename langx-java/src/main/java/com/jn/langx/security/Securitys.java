package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.util.Strings;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Securitys {
    private static void setup() {
        setupLangxProvider();
        setupGMSupports();
    }

    public static Provider getProvider(String name) {
        if (Strings.isNotBlank(name)) {
            return null;
        }
        return Security.getProvider(name);
    }

    public static void addProvider(Provider provider) {
        Security.addProvider(provider);
    }

    public static void setupLangxProvider() {
        addProvider(new LangxSecurityProvider());
    }

    public static boolean langxProviderInstalled() {
        return Security.getProvider(LangxSecurityProvider.NAME) != null;
    }

    private static void setupGMSupports() {
        Iterator<GmInitializer> iter = ServiceLoader.load(GmInitializer.class).iterator();
        while (iter.hasNext()) {
            GmInitializer initializer = iter.next();
            initializer.init();
        }
    }

    static {
        setup();
    }
}
