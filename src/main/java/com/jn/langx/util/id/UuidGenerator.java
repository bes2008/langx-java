package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;

import java.util.UUID;

public class UuidGenerator implements IdGenerator<Object> {
    @Override
    public String get(Object object) {
        return get();
    }
    public String get(){
        return UUID.randomUUID().toString();
    }
}
