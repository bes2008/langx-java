package com.jn.langx.util.id.snowflake;

import com.jn.langx.util.SystemPropertys;

public final class SystemEnvironmentSnowflakeIdWorkerProvider implements SnowflakeIdWorkerProvider {

    public static final String SYSTEM_ENVIRONMENT_SNOWFLAKE = "SYSTEM_ENVIRONMENT_SNOWFLAKE";

    private final static SnowflakeIdWorker worker;

    static {
        long workId = SystemPropertys.getAccessor().getLong("idgen.snowflake.workerId", 0L);
        long dataCenterId = SystemPropertys.getAccessor().getLong("idgen.snowflake.dataCenterId", 0L);
        worker = new CnblogsSnowflakeIdWorker(workId, dataCenterId);
    }

    @Override
    public SnowflakeIdWorker get() {
        return worker;
    }

    @Override
    public String getProviderId() {
        return SYSTEM_ENVIRONMENT_SNOWFLAKE;
    }
}
