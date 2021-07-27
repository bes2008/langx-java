package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class Securitys {
    private static volatile boolean providersLoaded = false;
    private static LangxSecurityProvider langxSecurityProvider;

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
        LangxSecurityProvider langxSecurityProvider = new LangxSecurityProvider();
        insertProvider(langxSecurityProvider);
        // insert 后，由于 langx provider  并没有经过 Oracle官方签名，所以只能用它的 message digest算法，所有涉及到的加解密的算法都不可用
        // 如果去官网申请的话：https://www.oracle.com/java/technologies/javase/getcodesigningcertificate.html 这里是教程

        // 采用借鸡生蛋的方式，将相关的属性加入到 JDK 默认提供的 providers中 ，这个方式也是不行的，虽然绕过了 Provider的认证过程，但因为放到的provider的 classloader 与你的类实际的classloader 不一样，所以仍然加载不到类。
        // 内置的 provider 的 classloader 要么是 bootstrap classloader,要么是 ext classloader
        Provider[] providers = Security.getProviders();
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        final ClassLoader extClassLoader = appClassLoader.getParent();
        final Provider expectedProvider = Collects.findFirst(Collects.asList(providers), new Predicate<Provider>() {
            @Override
            public boolean test(Provider provider) {
                // 不是 boostrap class loader
                ClassLoader providerCL = provider.getClass().getClassLoader();
                if (providerCL != null && providerCL != extClassLoader) {
                    return true;
                }
                return false;
            }
        });
        if (expectedProvider != null) {
            Map<String, String> properties = langxSecurityProvider.getProperties();
            Collects.forEach(properties, new Consumer2<String, String>() {
                @Override
                public void accept(String key, String value) {
                    expectedProvider.put(key, value);
                }
            });
        }
        Securitys.langxSecurityProvider = langxSecurityProvider;
    }

    public static boolean langxProviderInstalled() {
        return Security.getProvider(LangxSecurityProvider.NAME) != null;
    }

    public static LangxSecurityProvider getLangxSecurityProvider() {
        return langxSecurityProvider;
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
