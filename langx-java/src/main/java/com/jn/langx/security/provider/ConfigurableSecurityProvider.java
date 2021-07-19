package com.jn.langx.security.provider;

import com.jn.langx.annotation.NonNull;

public interface ConfigurableSecurityProvider {
    boolean hasAlgorithm(String type, String name);

    void addAlgorithm(String key, String value);

    void addAlgorithm(@NonNull String type, @NonNull String oid, @NonNull String className);
}
