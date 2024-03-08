package com.jn.langx.util.id.snowflake;

import com.jn.langx.environment.Environment;
import com.jn.langx.environment.EnvironmentAccessor;

public final class SystemEnvironmentSnowflakeIdWorkerProvider implements SnowflakeIdWorkerProvider {
    private Environment env = new SystemEnvironment();

    public static final String SYSTEM_ENVIRONMENT_SNOWFLAKE = "SYSTEM_ENVIRONMENT_SNOWFLAKE";

    private static volatile SnowflakeIdWorker worker = null;

    @Override
    public SnowflakeIdWorker get() {
        if (worker == null) {
            synchronized (SystemEnvironmentSnowflakeIdWorkerProvider.class) {
                if (worker == null) {
                    EnvironmentAccessor environmentAccessor = new EnvironmentAccessor();
                    environmentAccessor.setTarget(env);
                    long workId = environmentAccessor.getLong("idgen.snowflake.workerId", 0L);
                    long dataCenterId = environmentAccessor.getLong("idgen.snowflake.dataCenterId", 0L);

                    worker = new CnblogsSnowflakeIdWorker(workId, dataCenterId);
                    return worker;
                }
            }
        }
        return worker;
    }

    @Override
    public String getProviderId() {
        return SYSTEM_ENVIRONMENT_SNOWFLAKE;
    }
}
