package com.jn.langx.security;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.gm.GmInitializer;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

public class Securitys {
    private static final Logger logger = LoggerFactory.getLogger(Securitys.class);
    private static Set<String> GLOBAL_PROVIDERS = new HashSet<String>();
    private static SimpleIntegerCounter globalProviderIndexHolder = new SimpleIntegerCounter(0);

    private static void setup() {
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
        Security.addProvider(provider);
    }

    /**
     * 该方法只有在系统初始化时调用有效。
     *
     * @param providerClassName
     */
    public static void addGlobalProvider(String providerClassName) {
        GLOBAL_PROVIDERS.add(providerClassName);
    }

    public static void setupLangxProvider() {
        // 虽然代码里这么写了，但其实是添加不到全局的Provider里的，原因是没有对langx-java.jar进行签名
        addGlobalProvider(Reflects.getFQNClassName(LangxSecurityProvider.class));
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

    private static void setGlobalProviderIndex() {
        int index = 1;
        while (true) {
            String providerClassName = Security.getProperty("security.provider." + index);
            if (Strings.isBlank(providerClassName)) {
                break;
            }
            index++;
        }
        globalProviderIndexHolder.set(index);
    }

    private static void initialExtendsGlobalProviders() {
        setGlobalProviderIndex();
        Collects.forEach(GLOBAL_PROVIDERS, new Consumer<String>() {
            @Override
            public void accept(String providerClass) {
                Security.setProperty("security.provider." + globalProviderIndexHolder.getAndIncrement(1), providerClass);
            }
        });

        Collects.forEach(GLOBAL_PROVIDERS, new Consumer<String>() {
            @Override
            public void accept(String providerClass) {
                Class clazz = null;
                try {
                    clazz = ClassLoaders.loadClass(providerClass);
                    Provider provider = Reflects.<Provider>newInstance(clazz);
                    addProvider(provider);
                } catch (ClassNotFoundException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        });
    }

    static {
        setup();
        initialExtendsGlobalProviders();
        Provider[] all = Security.getProviders();
        System.out.println(all.length);
    }
}
