package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class Securitys {
    private static Set<Provider> GLOBAL_PROVIDERS = new LinkedHashSet<Provider>();
    private static volatile boolean providersLoaded = false;
    private static int global_providers_count = 0;

    public static void setup() {
        if (!providersLoaded) {
            synchronized (Securitys.class) {
                loadProviders();
            }
        }
        // 全局 Provider数量发生了改变
        if (global_providers_count != GLOBAL_PROVIDERS.size()) {
            Collects.forEach(GLOBAL_PROVIDERS, new Consumer<Provider>() {
                @Override
                public void accept(Provider provider) {
                    addProvider(provider);
                }
            });
            global_providers_count = GLOBAL_PROVIDERS.size();
        }
    }


    private static void loadProviders() {
        loadGMSupports();
        loadLangxProvider();
        providersLoaded = true;
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


    public static void loadLangxProvider() {
        // 虽然代码里这么写了，但其实是添加不到全局的Provider里的，原因是没有对langx-java.jar进行签名
        addProvider(new LangxSecurityProvider(), true);
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
