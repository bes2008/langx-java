package com.jn.langx.util.id.snowflake;

import com.jn.langx.environment.Environment;
import com.jn.langx.environment.EnvironmentAccessor;
import com.jn.langx.environment.SystemEnvironment;

public final class SystemEnvironmentSnowflakeIdWorkerProvider implements SnowflakeIdWorkerProvider {
    private Environment env = new SystemEnvironment();

    public static final String SYSTEM_ENVIRONMENT_SNOWFLAKE = "SYSTEM_ENVIRONMENT_SNOWFLAKE";

    @Override
    public SnowflakeIdWorker get() {
        EnvironmentAccessor environmentAccessor = new EnvironmentAccessor();
        environmentAccessor.setTarget(env);
        long workId = environmentAccessor.getLong("idgen.snowflake.workerId", 0L);
        long dataCenterId = environmentAccessor.getLong("idgen.snowflake.dataCenterId", 0L);

        return new CnBlogsSnowflakeIdWorker(workId, dataCenterId);
    }

    @Override
    public String getProviderId() {
        return SYSTEM_ENVIRONMENT_SNOWFLAKE;
    }
}
