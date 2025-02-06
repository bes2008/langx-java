package com.jn.langx;

/**
 * Nameable接口定义了名称管理的功能，继承自Named和NameAware接口
 * 这个接口适用于需要被命名并且能够感知自身名称的实体
 */
public interface Nameable extends Named, NameAware{
    /**
     * 设置实体的名称
     *
     * @param name 实体的新名称
     */
    @Override
    void setName(String name);

    /**
     * 获取实体的名称
     *
     * @return 实体的名称
     */
    @Override
    String getName();
}
