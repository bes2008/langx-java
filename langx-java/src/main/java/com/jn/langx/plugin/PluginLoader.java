package com.jn.langx.plugin;

import java.util.Iterator;
/**
 * 插件加载接口
 * 定义了加载指定类型插件的方法
 */
public interface PluginLoader {
    /**
     * 加载指定类型的插件
     *
     * @param <P> 插件类型，必须继承自Plugin
     * @param pluginClass 插件的类，用于指定要加载的插件类型
     * @return 插件迭代器，用于遍历加载的插件实例
     */
    <P extends Plugin> Iterator<P> load(Class<P> pluginClass);
}
