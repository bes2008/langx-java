package com.jn.langx.distributed.id.snowflake;

public interface SnowflakeIdWorkerProvider {
    SnowflakeIdWorker get();

    String getProviderId();
}
