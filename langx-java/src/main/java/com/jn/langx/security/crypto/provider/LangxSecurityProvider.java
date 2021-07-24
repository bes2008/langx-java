package com.jn.langx.security.crypto.provider;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;
import java.util.ServiceLoader;

public class LangxSecurityProvider extends Provider implements ConfigurableSecurityProvider {
    private static final Logger logger = LoggerFactory.getLogger(LangxSecurityProvider.class);
    public static final String NAME= "langx-java-security-provider";
    public LangxSecurityProvider(String name, double version, String info) {
        super(name, version, info);
        setup();
    }

    public LangxSecurityProvider(){
        this(NAME, 1.0d, "com.jn.langx");
    }

    public boolean hasAlgorithm(String type, String name) {
        return containsKey(type + "." + name) || containsKey("Alg.Alias." + type + "." + name);
    }

    public void addAlgorithm(String key, String value) {
        if (containsKey(key)) {
            logger.warn("duplicate provider key {} found, its value: {}", key, get(key));
            return;
        }
        put(key, value);
    }

    public void addAlgorithm(@NonNull String type, @NonNull String oid, @NonNull String className) {
        Preconditions.checkNotEmpty(type, "type is null or empty");
        Preconditions.checkNotEmpty(oid, "oid is null or empty");
        Preconditions.checkNotEmpty(className, "className is null or empty");

        addAlgorithm(type + "." + oid, className);
        addAlgorithm(type + ".OID." + oid, className);
    }



    private void setup(){
        load();
    }


    private void load(){
        Collects.forEach(ServiceLoader.load(LangxSecurityProviderConfigurer.class), new Consumer<LangxSecurityProviderConfigurer>() {
            @Override
            public void accept(LangxSecurityProviderConfigurer configurer) {
                configurer.configure(LangxSecurityProvider.this);
            }
        });
    }
}
