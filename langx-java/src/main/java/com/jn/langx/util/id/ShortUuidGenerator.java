package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;

/**
 * @since 4.4.6
 */
public class ShortUuidGenerator implements IdGenerator {
    @Override
    public String get(Object o) {
        return get();
    }

    @Override
    public String get() {
        return ShortUuid.shortUuid();
    }
}
