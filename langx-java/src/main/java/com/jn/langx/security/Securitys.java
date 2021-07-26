package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.util.Strings;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class Securitys {
    private static List<Provider> GLOBAL_PROVIDERS = new ArrayList<Provider>();


    public static void setup() {
        setupGMSupports();
        setupLangxProvider();
    }

    public static Provider getProvider(String name) {
        if (Strings.isNotBlank(name)) {
            return null;
        }
        return Security.getProvider(name);
    }

    /**
     * 调用 addProvider 只是将其添加到线程上下文中，
     * 如果想要作为全局的，需要将其添加到 ${JAVA_HOME}/jre/lib/security/java.security 文件中
     *
     * @param provider
     */
    public static void addProvider(Provider provider) {
        addProvider(provider, false);
    }

    public static void addProvider(Provider provider, boolean global) {
        Security.addProvider(provider);
        if (global) {
            GLOBAL_PROVIDERS.add(provider);
        }
    }


    public static void setupLangxProvider() {
        // 虽然代码里这么写了，但其实是添加不到全局的Provider里的，原因是没有对langx-java.jar进行签名
        addProvider(new LangxSecurityProvider(), true);
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
