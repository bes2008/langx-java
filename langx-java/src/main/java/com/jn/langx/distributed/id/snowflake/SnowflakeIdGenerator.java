package com.jn.langx.distributed.id.snowflake;

import com.jn.langx.IdGenerator;
import com.jn.langx.distributed.id.snowflake.SnowflakeIdWorkerProviderLoader;

public class SnowflakeIdGenerator implements IdGenerator {
    @Override
    public String get(Object object) {
        return SnowflakeIdWorkerProviderLoader.getProvider().get().get();
    }

    @Override
    public String get() {
        return get(null);
    }
}
