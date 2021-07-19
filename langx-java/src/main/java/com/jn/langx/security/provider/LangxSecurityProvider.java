package com.jn.langx.security.provider;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.security.Provider;

public class LangxSecurityProvider extends Provider implements ConfigurableSecurityProvider {

    public LangxSecurityProvider(String name, double version, String info) {
        super(name, version, info);
    }

    public boolean hasAlgorithm(String type, String name) {
        return containsKey(type + "." + name) || containsKey("Alg.Alias." + type + "." + name);
    }

    public void addAlgorithm(String key, String value) {
        if (containsKey(key)) {
            throw new IllegalStateException("duplicate provider key (" + key + ") found");
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

    }
}
