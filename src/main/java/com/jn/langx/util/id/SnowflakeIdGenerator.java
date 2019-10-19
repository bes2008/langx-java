package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;
import com.jn.langx.util.id.snowflake.SnowflakeIdWorkerProviderLoader;

public class SnowflakeIdGenerator implements IdGenerator {
    @Override
    public String get(Object object) {
        return Long.toBinaryString(SnowflakeIdWorkerProviderLoader.getProvider().get().nextId());
    }

    @Override
    public String get() {
        return get(null);
    }
}
