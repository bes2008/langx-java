package com.jn.langx.io.resource;

import com.jn.langx.Ordered;

import java.util.List;

/**
 * PatternResourceLoader 接口扩展了 ResourceLoader 接口，并添加了对模式路径加载的支持
 * 它允许通过模式匹配来加载多个资源，同时支持查询特定路径模式是否被支持
 *
 * @see ResourceLoader
 * @see Ordered
 */
public interface PatternResourceLoader extends ResourceLoader, Ordered {
    /**
     * 根据位置模式获取资源列表
     *
     * @param locationPattern 位置模式字符串，描述了资源的位置和匹配模式
     * @return 匹配位置模式的资源列表
     */
    List<Resource> getResources(String locationPattern);

    /**
     * 判断是否支持给定的位置模式
     *
     * @param locationPattern 位置模式字符串，描述了资源的位置和匹配模式
     * @return 如果位置模式被支持，则返回 true；否则返回 false
     */
    boolean isSupportedPattern(String locationPattern);
}
