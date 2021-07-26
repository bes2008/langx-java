package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.util.Strings;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Securitys {
    private static volatile boolean providersLoaded = false;

    public static void setup() {
        if (!providersLoaded) {
            synchronized (Securitys.class) {
                loadProviders();
            }
        }
    }


    private static void loadProviders() {
        loadGMSupports();
        loadLangxProvider();
    }

    public static Provider getProvider(String name) {
        if (Strings.isNotBlank(name)) {
            return null;
        }
        return Security.getProvider(name);
    }

    /**
     * 调用 addProvider 将其添加到全局，
     * 虽然添加了，但不一定会被用上，使用的顺序，
     * 其顺序由 ${JAVA_HOME}/jre/lib/security/java.security 文件来控制
     */
    public static void addProvider(Provider provider) {
        Security.addProvider(provider);
    }

    public static void insertProvider(Provider provider) {
        insertProviderAt(provider, 1);
    }

    public static void insertProviderAt(Provider provider, int position) {
        Security.insertProviderAt(provider, position);
    }

    public static void loadLangxProvider() {
        insertProvider(new LangxSecurityProvider());
    }

    public static boolean langxProviderInstalled() {
        return Security.getProvider(LangxSecurityProvider.NAME) != null;
    }

    private static void loadGMSupports() {
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
