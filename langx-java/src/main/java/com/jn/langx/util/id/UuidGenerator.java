package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;

import java.util.UUID;

public class UuidGenerator implements IdGenerator<Object> {
    public static final UuidGenerator INSTANCE = new UuidGenerator();

    @Override
    public String get(Object object) {
        return UUID.randomUUID().toString();

    }

    public String get() {
        return get(null);
    }
}
