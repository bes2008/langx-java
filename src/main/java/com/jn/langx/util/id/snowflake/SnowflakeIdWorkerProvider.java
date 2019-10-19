package com.jn.langx.util.id.snowflake;

public interface SnowflakeIdWorkerProvider {
    SnowflakeIdWorker get();
    String getProviderId();
}
