package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;

/**
 * @since 4.5.3
 */
public class UlidGenerator implements IdGenerator {
    @Override
    public String get(Object o) {
        return get();
    }

    @Override
    public String get() {
        return new Ulid().nextULID();
    }
}
