package com.jn.langx.security.crypto.provider;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.LinkedCaseInsensitiveMap;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.spi.CommonServiceProvider;
import org.slf4j.Logger;

import java.security.Provider;
import java.util.Map;
import java.util.TreeMap;

/**
 * langx 没有经过签名，最好是只往里面放 message digest 算法，加密算法都是要求算法所在的Jar包进行过 签名的
 */
public class LangxSecurityProvider extends Provider implements ConfigurableSecurityProvider {
    public static final String NAME = "langx-java-security-provider";


    private Map<String, String> properties = new TreeMap<String, String>();
    private Map<String, String> propertiesBackup = new LinkedCaseInsensitiveMap();

    public LangxSecurityProvider(String name, double version, String info) {
        super(name, version, info);
        setup();
    }

    public LangxSecurityProvider() {
        this(NAME, 1.0d, "com.jn.langx");
    }

    public boolean hasAlgorithm(String type, String name) {
        return containsKey(type + "." + name) || containsKey("Alg.Alias." + type + "." + name);
    }

    @Override
    public void addAlgorithm(String key, Class spiClass) {
        this.addAlgorithm(key, Reflects.getFQNClassName(spiClass));
    }

    public void addAlgorithm(String key, String value) {
        if (containsKey(key)) {
            Logger logger = Loggers.getLogger(PKIs.class);
            logger.warn("duplicate provider key {} found, its value: {}", key, get(key));
            return;
        }
        put(key, value);
        properties.put(key, value);
        propertiesBackup.put(key, value);
    }

    public void addAlgorithmOid(@NonNull String type, @NonNull String oid, @NonNull Class spiClassName) {
        addAlgorithmOid(type, oid, Reflects.getFQNClassName(spiClassName));
    }

    public void addAlgorithmOid(@NonNull String type, @NonNull String oid, @NonNull String spiClassName) {
        Preconditions.checkNotEmpty(type, "type is null or empty");
        Preconditions.checkNotEmpty(oid, "oid is null or empty");
        Preconditions.checkNotEmpty(spiClassName, "the spi class name is null or empty for {}", type + "." + oid);

        addAlgorithm(type + "." + oid, spiClassName);
        addAlgorithm(type + ".OID." + oid, spiClassName);
    }

    public void addAlias(String name, String alias) {
        this.addAlgorithm("Alg.Alias." + name, alias);
    }

    @Override
    public void addHmacAlgorithm(String digestAlgo, Class hmacAlgoSpiClass, Class keyGenSpiClass) {
        this.addHmacAlgorithm(digestAlgo, Reflects.getFQNClassName(hmacAlgoSpiClass), Reflects.getFQNClassName(keyGenSpiClass));
    }

    public void addHmacAlgorithm(String digestAlgo, String hmacAlgoSpiClassName, String keyGenSpiClassName) {
        String hmacAlgorithm = "HMAC" + digestAlgo;

        this.addAlgorithm("Mac." + hmacAlgorithm, hmacAlgoSpiClassName);
        this.addAlias("Mac.HMAC-" + digestAlgo, hmacAlgorithm);
        this.addAlias("Mac.HMAC/" + digestAlgo, hmacAlgorithm);

        this.addAlgorithm("KeyGenerator." + hmacAlgorithm, keyGenSpiClassName);
        this.addAlias("KeyGenerator.HMAC-" + digestAlgo, hmacAlgorithm);
        this.addAlias("KeyGenerator.HMAC/" + digestAlgo, hmacAlgorithm);
    }


    public void addHmacOidAlias(String hmacOid, String digestAlgorithm) {
        String hmacAlgorithm = "HMAC" + digestAlgorithm;

        this.addAlias("Mac." + hmacOid, hmacAlgorithm);
        this.addAlias("KeyGenerator." + hmacOid, hmacAlgorithm);
    }

    private void setup() {
        load();
    }


    private void load() {
        Collects.forEach(CommonServiceProvider.loadService(LangxSecurityProviderConfigurer.class), new Consumer<LangxSecurityProviderConfigurer>() {
            @Override
            public void accept(LangxSecurityProviderConfigurer configurer) {
                configurer.configure(LangxSecurityProvider.this);
            }
        });
    }

    public Map<String, String> getProperties() {
        return properties;
    }


    public String findAlgorithm(String type, String algorithm) {
        String value = propertiesBackup.get(type + "." + algorithm);
        if (Strings.isNotEmpty(value)) {
            return value;
        }
        String alias = propertiesBackup.get("Alg.Alias." + type + "." + algorithm);
        if (Strings.isNotEmpty(alias)) {
            value = propertiesBackup.get(type+"."+alias);
        }
        return value;
    }
}
