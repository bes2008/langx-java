package com.jn.langx.util.id.snowflake;

/**
 * SnowflakeIdWorkerProvider接口定义了SnowflakeIdWorker对象的提供者应遵循的规范
 * 它的主要作用是提供一个统一的接口，用于获取SnowflakeIdWorker实例和提供者的唯一标识
 */
public interface SnowflakeIdWorkerProvider {
    /**
     * 获取SnowflakeIdWorker实例
     *
     * @return SnowflakeIdWorker实例，用于生成唯一的ID
     */
    SnowflakeIdWorker get();

    /**
     * 获取提供者的唯一标识符
     *
     * @return 提供者的唯一标识符字符串，用于区分不同的提供者
     */
    String getProviderId();
}
